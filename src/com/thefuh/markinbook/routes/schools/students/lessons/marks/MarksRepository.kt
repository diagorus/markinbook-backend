package com.thefuh.markinbook.routes.schools.students.lessons.marks

import com.thefuh.markinbook.data.mark.StudentMark
import com.thefuh.markinbook.data.mark.TeacherMark
import com.thefuh.markinbook.routes.schools.students.StudentEntity
import com.thefuh.markinbook.routes.schools.students.StudentsTable
import com.thefuh.markinbook.routes.schools.students.lessons.LessonEntity
import com.thefuh.markinbook.routes.schools.students.lessons.LessonsTable
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.HomeworkEntity
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.HomeworksTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and

class MarksRepository {
    fun add(student: StudentEntity, lesson: LessonEntity?, homework: HomeworkEntity?, value: Int): MarkEntity {
        return MarkEntity.new {
            this.student = student
            this.lesson = lesson
            this.homework = homework
            this.value = value
        }
    }

    fun getByStudentIdAndLesson(studentId: Int, lesson: LessonEntity): SizedIterable<MarkEntity> {
        return MarkEntity.find { (MarksTable.studentId eq studentId) and (MarksTable.lessonId eq lesson.id) }
    }

    fun getByStudentIdAndHomework(studentId: Int, homework: HomeworkEntity): SizedIterable<MarkEntity> {
       return MarkEntity.find { (MarksTable.studentId eq studentId) and (MarksTable.lessonId eq homework.id) }
    }
}

object MarksTable : IntIdTable() {
    val studentId = reference("studentId", StudentsTable)
    val lessonId = reference("lessonId", LessonsTable).nullable()
    val homeworkId = reference("homeworkId", HomeworksTable).nullable()
    val value = integer("mark")
}

class MarkEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MarkEntity>(MarksTable)

    var student by StudentEntity referencedOn MarksTable.studentId
    var lesson by LessonEntity optionalReferencedOn MarksTable.lessonId
    var homework by HomeworkEntity optionalReferencedOn MarksTable.homeworkId
    var value by MarksTable.value
}

fun MarkEntity.toTeacherMark(): TeacherMark {
    return TeacherMark(student.id.value, value)
}

fun SizedIterable<MarkEntity>.toTeacherMarks(): List<TeacherMark> {
    return map { it.toTeacherMark() }
}

fun MarkEntity.toStudentMark(): StudentMark {
    return StudentMark(value)
}