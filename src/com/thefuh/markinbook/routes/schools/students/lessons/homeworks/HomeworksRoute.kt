package com.thefuh.markinbook.routes.schools.students.lessons.homeworks

import com.thefuh.markinbook.DatabaseFactory.dbQuery
import com.thefuh.markinbook.auth.Role
import com.thefuh.markinbook.auth.withRole
import com.thefuh.markinbook.routes.schools.LessonsLocation
import com.thefuh.markinbook.routes.schools.LessonsLocation.Lesson.Homeworks.Add.Companion.ARG_TASK_DESCRIPTION
import com.thefuh.markinbook.routes.schools.students.lessons.LessonsRepository
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.tasks.TasksRepository
import com.thefuh.markinbook.utils.push.PushManager
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
fun Route.homeworks(
    lessonsRepository: LessonsRepository,
    homeworksRepository: HomeworksRepository,
    tasksRepository: TasksRepository,
    pushManager: PushManager,
) {
    authenticate {
        withRole(Role.TEACHER) {
            post<LessonsLocation.Lesson.Homeworks.Add> { homeworksAdd ->
                val lessonId = homeworksAdd.homeworks.lesson.lessonId
                val lessonEntity = dbQuery { lessonsRepository.getById(lessonId) }!!

                val taskDescription = call.receive<Parameters>()[ARG_TASK_DESCRIPTION]!!

                val addedHomework = dbQuery {
                    val addedHomework = homeworksRepository.add(lessonEntity)
                    tasksRepository.add(addedHomework, taskDescription)
                    homeworksRepository.getById(addedHomework.id.value)?.toTeacherHomework()
                }!!

                val groupId = dbQuery { lessonEntity.group.id.value }
                val homeworkId = dbQuery { addedHomework.id }
                val disciplineTitle = dbQuery { lessonEntity.discipline.title }
                pushManager.pushHomeworkAdded(groupId, homeworkId, disciplineTitle)
                call.respond(HttpStatusCode.OK, addedHomework)
            }
        }
    }
}