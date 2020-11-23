package com.thefuh.markinbook.routes.schools.students.homeworks

import com.thefuh.markinbook.database.DatabaseFactory.dbQuery
import com.thefuh.markinbook.database.tables.students.homeworks.HomeworksRepository
import com.thefuh.markinbook.database.tables.lessons.LessonsRepository
import com.thefuh.markinbook.routes.schools.LessonsLocation
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
fun Route.homeworks(
    lessonsRepository: LessonsRepository,
    homeworksRepository: HomeworksRepository,
) {
    post<LessonsLocation.Lesson.Homeworks.Add> { homeworksAdd ->
        val lessonId = homeworksAdd.homeworks.lesson.lessonId
        val lessonEntity = dbQuery { lessonsRepository.getById(lessonId) }
        if (lessonEntity == null) {
            //todo
            return@post
        }

        val addedHomework = dbQuery { homeworksRepository.add(lessonEntity) }
        call.respond(HttpStatusCode.OK, addedHomework)
    }
}