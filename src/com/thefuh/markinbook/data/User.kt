package com.thefuh.markinbook.data

import com.thefuh.markinbook.auth.Role
import io.ktor.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val email: String,
    val passwordHash: String,
    val role: Role,
) : Principal