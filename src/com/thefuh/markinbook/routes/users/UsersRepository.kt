package com.thefuh.markinbook.routes.users

import com.thefuh.markinbook.auth.Role
import com.thefuh.markinbook.utils.PGEnum
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class UsersRepository {

    fun add(email: String, passwordHash: String, role: Role): UserEntity {
        return UserEntity.new {
            this.email = email
            this.passwordHash = passwordHash
            this.role = role
        }
    }

    fun getByEmail(email: String): UserEntity? {
        return UserEntity.find { UsersTable.email eq email }.firstOrNull()
    }

    fun getById(id: Int): UserEntity? {
        return UserEntity.findById(id)
    }
}

object UsersTable : IntIdTable() {
    val email = text("email").uniqueIndex()
    val passwordHash = text("password_hash")
    val role = customEnumeration(
        "role",
        "Role",
        { Role.valueOf(it as String) },
        { PGEnum("Role", it) }
    )
}

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable)

    var email by UsersTable.email
    var passwordHash by UsersTable.passwordHash
    var role by UsersTable.role
}