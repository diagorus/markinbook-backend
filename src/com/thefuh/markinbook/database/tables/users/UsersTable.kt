package com.thefuh.markinbook.database.tables.users

import com.thefuh.markinbook.auth.Role
import com.thefuh.markinbook.utils.PGEnum
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object UsersTable : IntIdTable() {
    val email = text("email").uniqueIndex()
    val passwordHash = text("password_hash")
    val role = customEnumeration(
        "role",
        "Role",
        { Role.valueOf(it as String) },
        { PGEnum("FooEnum", it) }
    )
}