package com.thefuh.markinbook.database.tables.schools.students

import org.jetbrains.exposed.sql.SizedIterable

class StudentsRepository {
    fun getAll(): SizedIterable<StudentEntity> {
        return StudentEntity.all()
    }

    fun add(firstName: String, lastName: String, middleName: String): StudentEntity {
        return StudentEntity.new {
            this.firstName = firstName
            this.lastName = lastName
            this.middleName = middleName
        }
    }

    fun getById(studentId: Int): StudentEntity? {
        return StudentEntity.findById(studentId)
    }
}