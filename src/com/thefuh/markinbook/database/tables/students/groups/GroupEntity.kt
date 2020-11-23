package com.thefuh.markinbook.database.tables.students.groups

import com.thefuh.markinbook.database.tables.students.StudentEntity
import com.thefuh.markinbook.database.tables.students.StudentsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class GroupEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GroupEntity>(GroupsTable)

    var title by GroupsTable.title
    var schoolId by GroupsTable.schoolId

    val students by StudentEntity referrersOn StudentsTable.groupId
}