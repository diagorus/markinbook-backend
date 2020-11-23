package com.thefuh.markinbook.database.tables.students.groups

import com.thefuh.markinbook.database.tables.schools.SchoolEntity
import org.jetbrains.exposed.sql.SizedIterable

class GroupsRepository {

    fun add(
        title: String,
        schoolEntity: SchoolEntity,
    ): GroupEntity {
        return GroupEntity.new {
            this.title = title
            this.schoolId = schoolEntity.id
        }
    }

    fun getAllBySchool(schoolEntity: SchoolEntity): SizedIterable<GroupEntity> {
        return GroupEntity.find { GroupsTable.schoolId eq schoolEntity.id }
    }

    fun getById(id: Int): GroupEntity? {
        return GroupEntity.findById(id)
    }
}