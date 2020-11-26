package com.thefuh.markinbook.database.tables.students

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class StudentEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StudentEntity>(StudentsTable)

    var firstName by StudentsTable.firstName
    var lastName by StudentsTable.lastName
    var profileImage by StudentsTable.profileImage
    var schoolId by StudentsTable.schoolId
    var groupId by StudentsTable.groupId
}