package com.thefuh.markinbook.routes.schools.teachers

import com.thefuh.markinbook.routes.schools.SchoolEntity
import com.thefuh.markinbook.routes.schools.SchoolsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

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

object TeachersTable : IntIdTable() {

    val firstName = text("firstName")
    val lastName = text("lastName")

    val schoolId = reference("schoolId", SchoolsTable)
}

class TeacherEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TeacherEntity>(TeachersTable)

    var firstName by TeachersTable.firstName
    var lastName by TeachersTable.lastName
    var schoolId by TeachersTable.schoolId
}