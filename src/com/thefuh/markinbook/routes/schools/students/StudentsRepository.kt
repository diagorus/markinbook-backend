package com.thefuh.markinbook.routes.schools.students

import com.thefuh.markinbook.data.roles.Student
import com.thefuh.markinbook.routes.schools.SchoolEntity
import com.thefuh.markinbook.routes.schools.SchoolsTable
import com.thefuh.markinbook.routes.schools.groups.GroupEntity
import com.thefuh.markinbook.routes.schools.groups.GroupsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
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

object StudentsTable : IntIdTable() {
    val firstName = text("firstName")
    val lastName = text("lastName")
    val profileImage = text("profileImage").nullable()

    val schoolId = reference("schoolId", SchoolsTable)
    val groupId = reference("groupId", GroupsTable)
}

class StudentEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StudentEntity>(StudentsTable)

    var firstName by StudentsTable.firstName
    var lastName by StudentsTable.lastName
    var profileImage by StudentsTable.profileImage
    var schoolId by StudentsTable.schoolId
    var groupId by StudentsTable.groupId
}

fun SizedIterable<StudentEntity>.toStudents(): List<Student> {
    return map { it.toStudent() }
}

fun StudentEntity.toStudent(): Student {
    return Student(
        id.value,
        firstName,
        lastName,
        schoolId.value,
        groupId.value,
        profileImage
    )
}