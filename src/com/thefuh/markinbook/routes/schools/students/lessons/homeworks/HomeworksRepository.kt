package com.thefuh.markinbook.routes.schools.students.lessons.homeworks

import com.thefuh.markinbook.data.lesson.homework.StudentHomework
import com.thefuh.markinbook.data.lesson.homework.TeacherHomework
import com.thefuh.markinbook.routes.schools.groups.GroupEntity
import com.thefuh.markinbook.routes.schools.students.lessons.LessonEntity
import com.thefuh.markinbook.routes.schools.students.lessons.LessonsTable
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.tasks.TaskEntity
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.tasks.TasksTable
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.tasks.toTasks
import com.thefuh.markinbook.routes.schools.students.lessons.marks.MarkEntity
import com.thefuh.markinbook.routes.schools.students.lessons.marks.MarksTable
import com.thefuh.markinbook.routes.schools.students.lessons.marks.toTeacherMarks
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class HomeworksRepository {

    fun add(lesson: LessonEntity): HomeworkEntity {
        return HomeworkEntity.new {
            this.lesson = lesson
        }
    }

    fun getById(id: Int): HomeworkEntity? {
        return HomeworkEntity.findById(id)
    }
}

object HomeworksTable : IntIdTable() {
    val lessonId = reference("lessonId", LessonsTable)
}

class HomeworkEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<HomeworkEntity>(HomeworksTable)

    var lesson by LessonEntity referencedOn HomeworksTable.lessonId
    val tasks by TaskEntity referrersOn TasksTable.homeworkId
    val marks by MarkEntity optionalReferrersOn MarksTable.homeworkId
}

fun HomeworkEntity.toTeacherHomework(): TeacherHomework {
    return TeacherHomework(
        id.value,
        marks.toTeacherMarks(),
        tasks.toTasks(),
    )
}

fun HomeworkEntity.toStudentHomework(studentId: Int): StudentHomework {
    return StudentHomework(
        id.value,
        marks.find { it.student.id.value == studentId }?.value,
        tasks.toTasks(),
    )
}