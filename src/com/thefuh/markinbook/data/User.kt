package com.thefuh.markinbook.data

import com.thefuh.markinbook.auth.Role

data class User(
    val id: Int,
    val email: String,
    val passwordHash: String,
    val role: Role,
)