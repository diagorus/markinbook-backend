package com.thefuh.markinbook.routes.schools.students.lessons

import com.thefuh.markinbook.data.lesson.StudentLesson
import com.thefuh.markinbook.data.lesson.TeacherLesson
import com.thefuh.markinbook.routes.schools.disciplines.DisciplineEntity
import com.thefuh.markinbook.routes.schools.disciplines.DisciplinesTable
import com.thefuh.markinbook.routes.schools.disciplines.toDiscipline
import com.thefuh.markinbook.routes.schools.groups.GroupEntity
import com.thefuh.markinbook.routes.schools.groups.GroupsTable
import com.thefuh.markinbook.routes.schools.groups.toGroup
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.HomeworkEntity
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.HomeworksTable
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.toStudentHomework
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.toTeacherHomework
import com.thefuh.markinbook.routes.schools.students.lessons.marks.MarkEntity
import com.thefuh.markinbook.routes.schools.students.lessons.marks.MarksTable
import com.thefuh.markinbook.routes.schools.students.lessons.marks.toStudentMark
import com.thefuh.markinbook.routes.schools.students.lessons.marks.toTeacherMarks
import com.thefuh.markinbook.routes.schools.teachers.TeacherEntity
import com.thefuh.markinbook.routes.schools.teachers.TeachersTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and

class LessonsRepository {

    fun add(
        teacher: TeacherEntity,
        group: GroupEntity,
        discipline: DisciplineEntity,
        start: Long,
        durationInMinutes: Int,
    ): LessonEntity {
        return LessonEntity.new {
            this.teacher = teacher
            this.group = group
            this.discipline = discipline
            this.start = start
            this.durationInMinutes = durationInMinutes
        }
    }

    fun getAllForWeekForTeacher(
        teacherId: Int,
        weekStartMillis: Long,
        weekEndMillis: Long
    ): SizedIterable<LessonEntity> {
        return LessonEntity.find {
            (LessonsTable.teacherId eq teacherId) and
                    (LessonsTable.start greaterEq weekStartMillis) and
                    (LessonsTable.start lessEq weekEndMillis)
        }
    }

    fun getAllForWeekForGroup(
        groupId: Int,
        weekStartMillis: Long,
        weekEndMillis: Long
    ): SizedIterable<LessonEntity> {
        return LessonEntity.find {
            (LessonsTable.groupId eq groupId) and
                    (LessonsTable.start greaterEq weekStartMillis) and
                    (LessonsTable.start lessEq weekEndMillis)
        }
    }


    fun getAllByTeacherId(teacherId: Int): SizedIterable<LessonEntity> {
        return LessonEntity.find { LessonsTable.teacherId eq teacherId }
    }

    fun getById(id: Int): LessonEntity? {
        return LessonEntity.findById(id)
    }
}

object LessonsTable : IntIdTable() {
    val teacherId = reference("teacherId", TeachersTable)
    val groupId = reference("groupId", GroupsTable)
    val disciplineId = reference("disciplineId", DisciplinesTable)
    val start = long("start")
    val durationInMinutes = integer("durationInMinutes")
    val homeworkId = reference("homeworkId", HomeworksTable).nullable()
    val location = text("location")
    val mark = integer("mark").nullable()
}

class LessonEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LessonEntity>(LessonsTable)

    var teacher by TeacherEntity referencedOn LessonsTable.teacherId
    var group by GroupEntity referencedOn LessonsTable.groupId
    var discipline by DisciplineEntity referencedOn LessonsTable.disciplineId
    var start by LessonsTable.start
    var durationInMinutes by LessonsTable.durationInMinutes
    var homework by HomeworkEntity optionalReferencedOn LessonsTable.homeworkId
    val marks by MarkEntity optionalReferrersOn MarksTable.lessonId
}

fun LessonEntity.toTeacherLesson(): TeacherLesson {
    return TeacherLesson(
        group.toGroup(),
        discipline.toDiscipline(),
        start,
        durationInMinutes,
        homework?.toTeacherHomework(),
        marks.toTeacherMarks()
    )
}

fun SizedIterable<LessonEntity>.toTeacherLessons(): List<TeacherLesson> {
    return map { it.toTeacherLesson() }
}

fun LessonEntity.toStudentLesson(studentId: Int): StudentLesson {
    return StudentLesson(
        group.toGroup(),
        discipline.toDiscipline(),
        start,
        durationInMinutes,
        homework?.toStudentHomework(studentId),
        marks.find { it.student.id.value == studentId }?.toStudentMark()
    )
}

fun SizedIterable<LessonEntity>.toStudentLessons(studentId: Int): List<StudentLesson> {
    return map { it.toStudentLesson(studentId) }
}
