package com.thefuh.markinbook.routes.schools.students

import com.thefuh.markinbook.database.DatabaseFactory.dbQuery
import com.thefuh.markinbook.database.tables.schools.students.StudentsRepository
import com.thefuh.markinbook.routes.schools.SchoolsLocation.School.Students
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

    get<Students.Student> { student ->
        val foundSchool = dbQuery { studentsRepository.getById(student.studentId)?.toStudent() }
        if (foundSchool == null) {
            //todo
        } else {
            call.respond(HttpStatusCode.OK, foundSchool)
        }
    }
}