package com.thefuh.markinbook.database.tables.teachers

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TeacherEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TeacherEntity>(TeachersTable)

    var firstName by TeachersTable.firstName
    var lastName by TeachersTable.lastName
    var schoolId by TeachersTable.schoolId
}