package com.thefuh.markinbook.routes.schools.groups

import com.thefuh.markinbook.routes.schools.SchoolEntity
import com.thefuh.markinbook.routes.schools.SchoolsTable
import com.thefuh.markinbook.routes.schools.students.StudentEntity
import com.thefuh.markinbook.routes.schools.students.StudentsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
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

object GroupsTable : IntIdTable() {
    val title = text("title")
    val schoolId = reference("schoolId", SchoolsTable)
}

class GroupEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GroupEntity>(GroupsTable)

    var title by GroupsTable.title
    var schoolId by GroupsTable.schoolId

    val students by StudentEntity referrersOn StudentsTable.groupId
}