package com.thefuh.markinbook.routes.schools.groups

import com.thefuh.markinbook.data.Group
import com.thefuh.markinbook.utils.DatabaseFactory.dbQuery
import com.thefuh.markinbook.routes.schools.SchoolsRepository
import com.thefuh.markinbook.routes.schools.SchoolsLocation.School.Groups
import com.thefuh.markinbook.routes.schools.students.toStudents
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.SizedIterable

@KtorExperimentalLocationsAPI
fun Route.groups(
    schoolsRepository: SchoolsRepository,
    groupsRepository: GroupsRepository,
) {
    get<Groups> { groupsGet ->
        val schoolId = groupsGet.school.schoolId
        val schoolEntity = dbQuery { schoolsRepository.getById(schoolId) }!!

        val groupsInSchool = dbQuery { groupsRepository.getAllBySchool(schoolEntity).toGroups() }
        call.respond(HttpStatusCode.OK, groupsInSchool)
    }
    get<Groups.Group> { groupGet ->
        val group = dbQuery { groupsRepository.getById(groupGet.groupId)?.toGroup() }
        call.respond(HttpStatusCode.OK, group!!)
    }
    post<Groups.Add> { groupsAdd ->
        val schoolId = groupsAdd.groups.school.schoolId
        val schoolEntity = dbQuery { schoolsRepository.getById(schoolId)!! }

        val formParams = call.receiveParameters()
        val title = formParams[Groups.Add.ARG_TITLE]!!

        val newGroup = dbQuery { groupsRepository.add(title, schoolEntity).toGroup() }
        call.respond(HttpStatusCode.OK, newGroup)
    }
}

fun GroupEntity.toGroup(): Group {
    return Group(
        id.value,
        title,
        students.toStudents()
    )
}

fun SizedIterable<GroupEntity>.toGroups(): List<Group> {
    return map { it.toGroup() }
}