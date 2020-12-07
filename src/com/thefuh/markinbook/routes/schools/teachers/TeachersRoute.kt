package com.thefuh.markinbook.routes.schools.teachers

import com.thefuh.markinbook.DatabaseFactory
import com.thefuh.markinbook.auth.UserPrincipal
import com.thefuh.markinbook.routes.schools.TeachersLocation
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
fun Route.teachers(teachersRepository: TeachersRepository) {
    authenticate {
        get<TeachersLocation.Current> {
            val userId = call.principal<UserPrincipal>()?.userId!!

            val currentStudent = DatabaseFactory.dbQuery { teachersRepository.getById(userId)?.toTeacher() }!!
            call.respond(HttpStatusCode.OK, currentStudent)
        }
    }
}