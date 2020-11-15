package com.thefuh.markinbook.routes.schools.students.lessons

import com.thefuh.markinbook.data.Day
import com.thefuh.markinbook.database.DatabaseFactory.dbQuery
import com.thefuh.markinbook.database.tables.schools.disciplines.DisciplinesRepository
import com.thefuh.markinbook.database.tables.schools.students.StudentsRepository
import com.thefuh.markinbook.database.tables.schools.students.groups.GroupsRepository
import com.thefuh.markinbook.database.tables.schools.students.lessons.LessonsRepository
import com.thefuh.markinbook.routes.schools.SchoolsLocation.Students.Student.Lessons
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.routing.get
import java.util.*

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
    get<Lessons.ByDays> { byDays ->
        val studentId = byDays.lessons.student.studentId

        val calendar = Calendar.getInstance().apply {
            set(Calendar.MILLISECOND, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.WEEK_OF_YEAR, byDays.week)
            set(Calendar.YEAR, byDays.year)
        }

        val mondayStartMillis = calendar.timeInMillis
        val tuesdayStartMillis = calendar.apply {
            add(Calendar.DAY_OF_WEEK, 1)
        }
            .timeInMillis
        val wednesdayStartMillis = calendar.apply {
            add(Calendar.DAY_OF_WEEK, 1)
        }
            .timeInMillis
        val thursdayStartMillis = calendar.apply {
            add(Calendar.DAY_OF_WEEK, 1)
        }
            .timeInMillis
        val fridayStartMillis = calendar.apply {
            add(Calendar.DAY_OF_WEEK, 1)
        }
            .timeInMillis
        val saturdayStartMillis = calendar.apply {
            add(Calendar.DAY_OF_WEEK, 1)
        }
            .timeInMillis
        val sundayStartMillis = calendar.apply {
            add(Calendar.DAY_OF_WEEK, 1)
        }
            .timeInMillis
        val nextMondayStartMillis = calendar.apply {
            add(Calendar.DAY_OF_WEEK, 1)
        }
            .timeInMillis

        val lessonsByWeekday = dbQuery {
            val allLessonsInWeek =
                lessonsRepository.getAllForWeek(studentId, mondayStartMillis, nextMondayStartMillis).toLessons()
            allLessonsInWeek.groupBy {
                when (it.start) {
                    in mondayStartMillis until tuesdayStartMillis -> {
                        Day.MONDAY
                    }
                    in tuesdayStartMillis until wednesdayStartMillis -> {
                        Day.TUESDAY
                    }
                    in wednesdayStartMillis until thursdayStartMillis -> {
                        Day.WEDNESDAY
                    }
                    in thursdayStartMillis until fridayStartMillis -> {
                        Day.THURSDAY
                    }
                    in fridayStartMillis until saturdayStartMillis -> {
                        Day.FRIDAY
                    }
                    in saturdayStartMillis until sundayStartMillis -> {
                        Day.SATURDAY
                    }
                    in sundayStartMillis until nextMondayStartMillis -> {
                        Day.SUNDAY
                    }
                    else -> null
                }
            }
        }
        call.respond(HttpStatusCode.OK, lessonsByWeekday)
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
        val groupEntity = dbQuery { groupsRepository.getById(groupId) }
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