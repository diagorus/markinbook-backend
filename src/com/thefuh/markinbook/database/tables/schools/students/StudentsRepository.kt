package com.thefuh.markinbook.database.tables.schools.students

class StudentsRepository {
    fun getAll(): List<StudentEntity> {
        return StudentEntity.all().toList()
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