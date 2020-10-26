package com.thefuh.markinbook.database.tables.schools.students.groups

class GroupsRepository {

    fun getById(id: Int): GroupEntity? {
        return GroupEntity.findById(id)
    }
}