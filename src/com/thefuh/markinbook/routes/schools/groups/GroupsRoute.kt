package com.thefuh.markinbook.routes.schools.groups

import com.thefuh.markinbook.database.DatabaseFactory.dbQuery
import com.thefuh.markinbook.database.tables.schools.SchoolsRepository
import com.thefuh.markinbook.database.tables.students.groups.GroupsRepository
import com.thefuh.markinbook.routes.schools.SchoolsLocation.School.Groups
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
fun Route.groups(
    schoolsRepository: SchoolsRepository,
    groupsRepository: GroupsRepository,
) {
    get<Groups> { groupsGet ->
        val schoolId = groupsGet.school.schoolId
        val schoolEntity = dbQuery { schoolsRepository.getById(schoolId) }

        if (schoolEntity == null) {
            //todo
            return@get
        }

        val groupsInSchool = dbQuery { groupsRepository.getAllBySchool(schoolEntity).toGroups() }
        call.respond(HttpStatusCode.OK, groupsInSchool)
    }
    get<Groups.Group> { groupGet ->
        val group = dbQuery { groupsRepository.getById(groupGet.groupId)?.toGroup() }
        if (group == null) {
            //todo
        } else {
            call.respond(HttpStatusCode.OK, group)
        }
    }
    post<Groups.Add> { groupsAdd ->
        val schoolId = groupsAdd.groups.school.schoolId
        val schoolEntity = dbQuery { schoolsRepository.getById(schoolId) }

        if (schoolEntity == null) {
            //todo
            return@post
        }

        val formParams = call.receiveParameters()
        val title = formParams[Groups.Add.ARG_TITLE]

        if (title.isNullOrEmpty()) {
            //todo
            return@post
        }

        val newGroup = dbQuery { groupsRepository.add(title, schoolEntity).toGroup() }
        call.respond(HttpStatusCode.OK, newGroup)
    }
}