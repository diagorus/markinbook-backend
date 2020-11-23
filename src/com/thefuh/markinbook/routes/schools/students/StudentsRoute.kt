package com.thefuh.markinbook.routes.schools.students

import com.thefuh.markinbook.auth.UserSession
import com.thefuh.markinbook.database.DatabaseFactory.dbQuery
import com.thefuh.markinbook.database.tables.students.StudentsRepository
import com.thefuh.markinbook.routes.schools.StudentsLocation
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

@KtorExperimentalLocationsAPI
fun Route.students(
    studentsRepository: StudentsRepository,
) {
    authenticate {
        get<StudentsLocation> {
            val allStudents = dbQuery { studentsRepository.getAll().toStudents() }
            call.respond(HttpStatusCode.OK, allStudents)
        }
        get<StudentsLocation.Student> { student ->
            val foundStudent = dbQuery { studentsRepository.getById(student.studentId)?.toStudent() }
            if (foundStudent == null) {
                //todo
            } else {
                call.respond(HttpStatusCode.OK, foundStudent)
            }
        }
        get<StudentsLocation.Current> {
            val userId = call.principal<UserSession>()?.userId
            if (userId == null) {
                //todo
                return@get
            }
            val currentStudent = dbQuery { studentsRepository.getById(userId)?.toStudent() }
            if (currentStudent == null) {
                //todo
                return@get
            } else {
                call.respond(HttpStatusCode.OK, currentStudent)
            }
        }
    }
}