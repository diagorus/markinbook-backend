package com.thefuh.markinbook.routes.schools.students.lessons.homeworks

import com.thefuh.markinbook.routes.schools.students.lessons.LessonsRepository
import io.ktor.locations.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
fun Route.homeworks(
    lessonsRepository: LessonsRepository,
    homeworksRepository: HomeworksRepository,
) {
//    post<LessonsLocation.Lesson.Homeworks.Add> { homeworksAdd ->
//        val lessonId = homeworksAdd.homeworks.lesson.lessonId
//        val lessonEntity = dbQuery { lessonsRepository.getById(lessonId) }!!
//
//        val addedHomework = dbQuery { homeworksRepository.add(lessonEntity) }
//        call.respond(HttpStatusCode.OK, addedHomework)
//    }
}