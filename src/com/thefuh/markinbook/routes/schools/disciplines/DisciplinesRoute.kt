package com.thefuh.markinbook.routes.schools.disciplines

import com.thefuh.markinbook.database.DatabaseFactory.dbQuery
import com.thefuh.markinbook.database.tables.schools.disciplines.DisciplinesRepository
import com.thefuh.markinbook.database.tables.schools.SchoolsRepository
import com.thefuh.markinbook.routes.schools.SchoolsLocation.School.Disciplines
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val DISCIPLINE_PROBLEMS = "Problems adding discipline"

@KtorExperimentalLocationsAPI
fun Route.disciplines(
    schoolsRepository: SchoolsRepository,
    disciplinesRepository: DisciplinesRepository
) {
    get<Disciplines> {
        val allDisciplines = dbQuery { disciplinesRepository.getAll().toDisciplines() }
        call.respond(HttpStatusCode.OK, allDisciplines)
    }

//    authenticate("jwt") {
    post<Disciplines.Add> { disciplinesAdd ->
//            val user = call.sessions.get<MySession>()?.let {
//                db.findUser(it.userId)
//            }
//            if (user == null) {
//                call.respond(
//                    HttpStatusCode.BadRequest, "Problems retrieving User"
//                )
//                return@post
//            }

        val school = try {
           dbQuery { schoolsRepository.getById(disciplinesAdd.disciplines.school.schoolId) }
        } catch (e: Exception) {
            application.log.error("Failed to get School", e)
            call.respond(HttpStatusCode.BadRequest, DISCIPLINE_PROBLEMS)

            null
        }

        if (school == null) {
            call.respond(HttpStatusCode.BadRequest, "Problems retrieving school")
            return@post
        }

        val formParams = call.receive<Parameters>()
        val title = formParams[Disciplines.Add.TITLE]

        if (title == null) {
            //todo
        } else {
            try {
                val newDiscipline = dbQuery { disciplinesRepository.add(title, school).toDiscipline() }
                call.respond(HttpStatusCode.OK, newDiscipline)
            } catch (e: Throwable) {
                application.log.error("Failed to add Discipline", e)
                call.respond(HttpStatusCode.BadRequest, DISCIPLINE_PROBLEMS)
            }
        }
    }

    get<Disciplines.Discipline> { discipline ->
        val foundDiscipline = dbQuery { disciplinesRepository.getById(discipline.disciplineId)?.toDiscipline() }
        if (foundDiscipline == null) {
            //todo
        } else {
            call.respond(HttpStatusCode.OK, foundDiscipline)
        }
    }
//        get<DisciplineRoute> {
//            val user = call.sessions.get<MySession>()?.let { db.findUser(it.userId) }
//            if (user == null) {
//                call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
//                return@get
//            }
//            try {
//                val todos = db.getTodos(user.userId)
//                call.respond(todos)
//            } catch (e: Throwable) {
//                application.log.error("Failed to get Todos", e)
//                call.respond(HttpStatusCode.BadRequest, "Problems getting Todos")
//            }
//        }
//    }
}