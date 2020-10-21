package com.thefuh.markinbook.routes.users

import com.thefuh.markinbook.API_VERSION
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("$API_VERSION/users")
class UsersLocation {

    @Location("/signUp")
    data class SignUp(val users: UsersLocation) {
        companion object {
            const val EMAIL = "email"
            const val PASSWORD = "password"
            const val ROLE = "role"
        }
    }

    @Location("/signIn")
    data class SignIn(val users: UsersLocation) {
        companion object {
            const val EMAIL = "email"
            const val PASSWORD = "password"
            const val ROLE = "role"
        }
    }

    @Location("/signOut")
    data class SignOut(val users: UsersLocation)
}