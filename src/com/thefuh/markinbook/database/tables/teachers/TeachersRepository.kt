package com.thefuh.markinbook.database.tables.teachers

import com.thefuh.markinbook.database.tables.schools.SchoolEntity

class TeachersRepository {

    fun add(
        id: Int,
        firstName: String,
        lastName: String,
        schoolEntity: SchoolEntity,
    ): TeacherEntity {
        return TeacherEntity.new(id) {
            this.firstName = firstName
            this.lastName = lastName
            this.schoolId = schoolEntity.id
        }
    }

    fun getById(studentId: Int): TeacherEntity? {
        return TeacherEntity.findById(studentId)
    }
}