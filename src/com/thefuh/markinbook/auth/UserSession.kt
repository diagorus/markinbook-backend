package com.thefuh.markinbook.auth

import io.ktor.auth.Principal

data class UserSession(val userId: Int, val role: Role): Principal