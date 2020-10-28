package com.thefuh.markinbook.database.tables.schools.students

import com.thefuh.markinbook.database.tables.schools.SchoolEntity
import com.thefuh.markinbook.database.tables.schools.students.groups.GroupEntity
import org.jetbrains.exposed.sql.SizedIterable

class StudentsRepository {
    fun getAll(): SizedIterable<StudentEntity> {
        return StudentEntity.all()
    }

    fun add(
        firstName: String,
        lastName: String,
        schoolEntity: SchoolEntity,
        groupEntity: GroupEntity,
    ): StudentEntity {
        return StudentEntity.new {
            this.firstName = firstName
            this.lastName = lastName
            this.schoolId = schoolEntity.id
            this.groupId = groupEntity.id
        }
    }

    fun getById(studentId: Int): StudentEntity? {
        return StudentEntity.findById(studentId)
    }
}