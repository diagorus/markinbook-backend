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

class MarksRepository {

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