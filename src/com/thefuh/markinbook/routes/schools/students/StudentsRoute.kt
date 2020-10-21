package com.thefuh.markinbook.routes.schools.students

import com.thefuh.markinbook.database.DatabaseFactory.dbQuery
import com.thefuh.markinbook.database.tables.schools.students.StudentsRepository
import com.thefuh.markinbook.routes.schools.SchoolsLocation.Students
import com.thefuh.markinbook.routes.schools.disciplines.DISCIPLINE_PROBLEMS
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
fun Route.students(
    studentsRepository: StudentsRepository,
) {
    get<Students> {
        val allStudents = dbQuery { studentsRepository.getAll().toStudents() }
        call.respond(HttpStatusCode.OK, allStudents)
    }

    post<Students.Add> {
        try {
            val formParams = call.receive<Parameters>()
            val firstName = formParams[Students.Add.ARG_FIRST_NAME]
            if (firstName == null) {
                //todo
                return@post
            }

            val lastName = formParams[Students.Add.ARG_LAST_NAME]
            if (lastName == null) {
                //todo
                return@post
            }

            val middleName = formParams[Students.Add.ARG_MIDDLE_NAME]
            if (middleName == null) {
                //todo
                return@post
            }

            val newSchool = dbQuery { studentsRepository.add(firstName, lastName, middleName).toStudent() }
            call.respond(HttpStatusCode.OK, newSchool)
        } catch (e: Throwable) {
            application.log.error("Failed to add School", e)
            call.respond(HttpStatusCode.BadRequest, DISCIPLINE_PROBLEMS)
        }
    }

    get<Students.Student> { student ->
        val foundSchool = dbQuery { studentsRepository.getById(student.studentId)?.toStudent() }
        if (foundSchool == null) {
            //todo
        } else {
            call.respond(HttpStatusCode.OK, foundSchool)
        }
    }
}