package com.thefuh.markinbook.auth

import io.ktor.auth.Principal

data class UserPrincipal(val userId: Int, val role: Role): Principal