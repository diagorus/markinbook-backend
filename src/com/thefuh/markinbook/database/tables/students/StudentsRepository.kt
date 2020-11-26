package com.thefuh.markinbook.database.tables.students

import com.thefuh.markinbook.database.tables.schools.SchoolEntity
import com.thefuh.markinbook.database.tables.students.groups.GroupEntity
import org.jetbrains.exposed.sql.SizedIterable

class StudentsRepository {
    fun getAll(): SizedIterable<StudentEntity> {
        return StudentEntity.all()
    }

    fun add(
        id: Int,
        firstName: String,
        lastName: String,
        schoolEntity: SchoolEntity,
        groupEntity: GroupEntity,
    ): StudentEntity {
        return StudentEntity.new(id) {
            this.firstName = firstName
            this.lastName = lastName
            this.schoolId = schoolEntity.id
            this.groupId = groupEntity.id
        }
    }

    fun updateImage(studentId: Int, image: String): StudentEntity? {
        return StudentEntity.findById(studentId)?.apply {
            this.profileImage = image
        }
    }

    fun getById(studentId: Int): StudentEntity? {
        return StudentEntity.findById(studentId)
    }
}