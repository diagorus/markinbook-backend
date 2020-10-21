package com.thefuh.markinbook.database.tables.users

import com.thefuh.markinbook.auth.Role

class UsersRepository {

    fun add(email: String, passwordHash: String, role: Role): UserEntity {
        return UserEntity.new {
            this.email = email
            this.passwordHash = passwordHash
            this.role = role
        }
    }

    fun findByEmail(email: String): UserEntity? {
        return UserEntity.find { UsersTable.email eq email }.firstOrNull()
    }

    fun findById(id: Int): UserEntity? {
        return UserEntity.findById(id)
    }
}