package com.thefuh.markinbook.auth

import com.thefuh.markinbook.data.User
import io.ktor.auth.*

data class UserSession(val userId: Int)