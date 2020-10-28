package com.thefuh.markinbook.routes.users

import com.thefuh.markinbook.API_NAME
import com.thefuh.markinbook.API_VERSION
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("$API_NAME/$API_VERSION/users")
class UsersLocation {

    @Location("/signUp")
    data class SignUp(val users: UsersLocation) {
        companion object {
            const val FIRST_NAME = "firstName"
            const val LAST_NAME = "lastName"
            const val SCHOOL_ID = "schoolId"
            const val GROUP_ID = "groupId"

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