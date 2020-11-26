package com.thefuh.markinbook.routes.schools.students.lessons

import com.thefuh.markinbook.auth.Role
import com.thefuh.markinbook.auth.UserSession
import com.thefuh.markinbook.auth.withAnyRole
import com.thefuh.markinbook.auth.withRole
import com.thefuh.markinbook.data.Day
import com.thefuh.markinbook.database.DatabaseFactory.dbQuery
import com.thefuh.markinbook.database.tables.schools.disciplines.DisciplinesRepository
import com.thefuh.markinbook.database.tables.students.groups.GroupsRepository
import com.thefuh.markinbook.database.tables.lessons.LessonsRepository
import com.thefuh.markinbook.database.tables.students.StudentsRepository
import com.thefuh.markinbook.database.tables.teachers.TeachersRepository
import com.thefuh.markinbook.routes.schools.LessonsLocation
import com.thefuh.markinbook.routes.schools.students.toStudent
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*

@KtorExperimentalLocationsAPI
fun Route.lessons(
    studentsRepository: StudentsRepository,
    teachersRepository: TeachersRepository,
    lessonsRepository: LessonsRepository,
    groupsRepository: GroupsRepository,
    disciplinesRepository: DisciplinesRepository,
) {
    authenticate {
        withAnyRole(Role.STUDENT, Role.TEACHER) {
            get<LessonsLocation.ByDays> { byDays ->
                val userSession = call.principal<UserSession>()

                if (userSession == null) {
                    //todo
                    return@get
                }

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
                    val allLessonsInWeek = if (userSession.role == Role.STUDENT) {
                       val student = studentsRepository.getById(userSession.userId)!!
                        lessonsRepository.getAllForWeekForGroup(student.groupId.value, mondayStartMillis, nextMondayStartMillis).toLessons()
                    } else {
                        lessonsRepository.getAllForWeekForTeacher(userSession.userId, mondayStartMillis, nextMondayStartMillis).toLessons()
                    }
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
        }
        withRole(Role.TEACHER) {
            post<LessonsLocation.Add> {
                val userId = call.principal<UserSession>()?.userId

                if (userId == null) {
                    //todo
                    return@post
                }

                val teacherEntity = teachersRepository.getById(userId)
                if (teacherEntity == null) {
                    //todo
                    return@post
                }

                val parameters = call.receiveParameters()

                val groupId = parameters[LessonsLocation.Add.ARG_GROUP_ID]?.toIntOrNull()
                if (groupId == null) {
                    //todo
                    return@post
                }
                val groupEntity = dbQuery { groupsRepository.getById(groupId) }
                if (groupEntity == null) {
                    //todo
                    return@post
                }

                val disciplineId = parameters[LessonsLocation.Add.ARG_DISCIPLINE_ID]?.toIntOrNull()
                if (disciplineId == null) {
                    //todo
                    return@post
                }
                val disciplineEntity = disciplinesRepository.getById(disciplineId)
                if (disciplineEntity == null) {
                    //todo
                    return@post
                }

                val start = parameters[LessonsLocation.Add.ARG_START]?.toLongOrNull()
                if (start == null) {
                    //todo
                    return@post
                }

                val durationInMinutes = parameters[LessonsLocation.Add.ARG_DURATION_IN_MINUTES]?.toIntOrNull()
                if (durationInMinutes == null) {
                    //todo
                    return@post
                }

                val addedLesson = dbQuery {
                    lessonsRepository.add(
                        teacherEntity,
                        groupEntity,
                        disciplineEntity,
                        start,
                        durationInMinutes
                    ).toLesson()
                }
                call.respond(HttpStatusCode.OK, addedLesson)
            }
        }
    }
}