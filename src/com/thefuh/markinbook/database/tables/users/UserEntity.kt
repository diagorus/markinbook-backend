package com.thefuh.markinbook.database.tables.users

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable)

    var email by UsersTable.email
    var passwordHash by UsersTable.passwordHash
    var role by UsersTable.role
}