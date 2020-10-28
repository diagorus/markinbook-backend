package com.thefuh.markinbook.routes.schools.students

import com.thefuh.markinbook.data.roles.Student
import com.thefuh.markinbook.database.tables.schools.students.StudentEntity
import org.jetbrains.exposed.sql.SizedIterable


fun SizedIterable<StudentEntity>.toStudents(): List<Student> {
    return map { it.toStudent() }
}

fun StudentEntity.toStudent(): Student {
    return Student(
        id.value,
        firstName,
        lastName
    )
}