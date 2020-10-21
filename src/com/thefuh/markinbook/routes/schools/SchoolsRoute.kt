package com.thefuh.markinbook.routes.schools

import com.thefuh.markinbook.database.DatabaseFactory.dbQuery
import com.thefuh.markinbook.database.tables.schools.SchoolsRepository
import com.thefuh.markinbook.routes.schools.disciplines.DISCIPLINE_PROBLEMS
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
fun Route.schools(
    schoolsRepository: SchoolsRepository,
) {
    get<SchoolsLocation> {
        val allDisciplines = dbQuery { schoolsRepository.getAll().toSchools() }
        call.respond(HttpStatusCode.OK, allDisciplines)
    }

    authenticate {
        post<SchoolsLocation.Add> {
            try {
                val formParams = call.receiveParameters()
                val title = formParams[SchoolsLocation.Add.TITLE]
                if (title == null) {
                    //todo
                } else {
                    val newSchool = dbQuery { schoolsRepository.add(title).toSchool() }
                    call.respond(HttpStatusCode.OK, newSchool)
                }
            } catch (e: Throwable) {
                application.log.error("Failed to add School", e)
                call.respond(HttpStatusCode.BadRequest, DISCIPLINE_PROBLEMS)
            }
        }

        get<SchoolsLocation.School> { school ->
            val foundSchool = dbQuery { schoolsRepository.getById(school.schoolId)?.toSchool() }
            if (foundSchool == null) {
                //todo
            } else {
                call.respond(HttpStatusCode.OK, foundSchool)
            }
        }
    }
}
