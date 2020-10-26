package com.thefuh.markinbook.routes.schools.students.lessons

import com.thefuh.markinbook.database.DatabaseFactory.dbQuery
import com.thefuh.markinbook.database.tables.schools.disciplines.DisciplinesRepository
import com.thefuh.markinbook.database.tables.schools.students.StudentsRepository
import com.thefuh.markinbook.database.tables.schools.students.groups.GroupsRepository
import com.thefuh.markinbook.database.tables.schools.students.lessons.LessonsRepository
import com.thefuh.markinbook.routes.schools.SchoolsLocation
import com.thefuh.markinbook.routes.schools.SchoolsLocation.Students.Student.Lessons
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.routing.get

@KtorExperimentalLocationsAPI
fun Route.lessons(
    studentsRepository: StudentsRepository,
    lessonsRepository: LessonsRepository,
    groupsRepository: GroupsRepository,
    disciplinesRepository: DisciplinesRepository,
) {
    get<Lessons> { lessons ->
        val studentId = lessons.student.studentId
        val studentsLessons = dbQuery { lessonsRepository.getAllByStudentId(studentId).toLessons() }
        call.respond(HttpStatusCode.OK, studentsLessons)
    }
    post<Lessons.Add> { lessonsAdd ->
        val studentId = lessonsAdd.lessons.student.studentId
        val studentEntity = studentsRepository.getById(studentId)
        if (studentEntity == null) {
            //todo
            return@post
        }

        val parameters = call.receiveParameters()

        val groupId = parameters[Lessons.Add.ARG_GROUP_ID]?.toIntOrNull()
        if (groupId == null) {
            //todo
            return@post
        }
        val groupEntity = groupsRepository.getById(groupId)
        if (groupEntity == null) {
            //todo
            return@post
        }

        val disciplineId = parameters[Lessons.Add.ARG_DISCIPLINE_ID]?.toIntOrNull()
        if (disciplineId == null) {
            //todo
            return@post
        }
        val disciplineEntity = disciplinesRepository.getById(disciplineId)
        if (disciplineEntity == null) {
            //todo
            return@post
        }

        val start = parameters[Lessons.Add.ARG_START]?.toLongOrNull()
        if (start == null) {
            //todo
            return@post
        }

        val durationInMinutes = parameters[Lessons.Add.ARG_DURATION_IN_MINUTES]?.toIntOrNull()
        if (durationInMinutes == null) {
            //todo
            return@post
        }

        val addedLesson = dbQuery {
            lessonsRepository.add(
                studentEntity,
                groupEntity,
                disciplineEntity,
                start,
                durationInMinutes
            ).toLesson()
        }
        call.respond(HttpStatusCode.OK, addedLesson)
    }
}