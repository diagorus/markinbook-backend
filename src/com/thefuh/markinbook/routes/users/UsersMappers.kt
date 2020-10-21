package com.thefuh.markinbook.routes.users

import com.thefuh.markinbook.data.User
import com.thefuh.markinbook.database.tables.users.UserEntity

fun UserEntity.toUser(): User {
    return User(
        id.value,
        email,
        passwordHash,
        role
    )
}