package com.thefuh.markinbook.routes.schools.students.lessons

import com.thefuh.markinbook.database.DatabaseFactory.dbQuery
import com.thefuh.markinbook.database.tables.schools.students.lessons.LessonsRepository
import com.thefuh.markinbook.routes.schools.SchoolsLocation
import com.thefuh.markinbook.routes.schools.SchoolsLocation.Students.Student.Lessons
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.routing.get

@KtorExperimentalLocationsAPI
fun Route.lessons(lessonsRepository: LessonsRepository) {
    get<Lessons> { lessons ->
        val studentsLessons = dbQuery { lessonsRepository.getAllByStudentId(lessons.student.studentId).toLessons() }
        call.respond(HttpStatusCode.OK, studentsLessons)
    }
}