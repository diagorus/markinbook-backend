package com.thefuh.markinbook.routes.users

import com.thefuh.markinbook.data.User

fun UserEntity.toUser(): User {
    return User(
        id.value,
        email,
        passwordHash,
        role
    )
}