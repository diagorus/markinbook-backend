package com.thefuh.markinbook.routes.schools.students.lessons.homeworks.tasks

import com.thefuh.markinbook.DatabaseFactory.dbQuery
import com.thefuh.markinbook.routes.schools.LessonsLocation
import com.thefuh.markinbook.routes.schools.LessonsLocation.Lesson.Homeworks.Homework.Tasks.Add.Companion.ARG_DESCRIPTION
import com.thefuh.markinbook.routes.schools.students.lessons.LessonsRepository
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.HomeworksRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
fun Route.tasks(
    homeworksRepository: HomeworksRepository,
    tasksRepository: TasksRepository,
) {
    post<LessonsLocation.Lesson.Homeworks.Homework.Tasks.Add> { tasksAdd ->
        val homeworkId = tasksAdd.tasks.homework.homeworkId
        val description = call.receive<Parameters>()[ARG_DESCRIPTION]!!
        val addedTask = dbQuery {
            val homeworkEntity = homeworksRepository.getById(homeworkId)!!
            tasksRepository.add(homeworkEntity, description)
        }
        call.respond(HttpStatusCode.OK, addedTask)
    }
}