package com.thefuh.markinbook.routes.users

import com.thefuh.markinbook.API_NAME
import com.thefuh.markinbook.API_VERSION
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("$API_NAME/$API_VERSION/users")
class UsersLocation {

    @Location("{role}/signUp")
    data class SignUp(val users: UsersLocation, val role: String) {
        companion object {
            const val FIRST_NAME = "firstName"
            const val LAST_NAME = "lastName"
            const val SCHOOL_ID = "schoolId"
            const val GROUP_ID = "groupId"

            const val EMAIL = "email"
            const val PASSWORD = "password"
        }
    }

    @Location("{role}/signIn")
    data class SignIn(val users: UsersLocation, val role: String) {
        companion object {
            const val EMAIL = "email"
            const val PASSWORD = "password"
        }
    }

    @Location("/signOut")
    data class SignOut(val users: UsersLocation)
}