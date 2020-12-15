package com.thefuh.markinbook.routes.schools.students.lessons

import com.thefuh.markinbook.auth.Role
import com.thefuh.markinbook.auth.UserPrincipal
import com.thefuh.markinbook.auth.withAnyRole
import com.thefuh.markinbook.auth.withRole
import com.thefuh.markinbook.data.Day
import com.thefuh.markinbook.DatabaseFactory.dbQuery
import com.thefuh.markinbook.routes.schools.LessonsLocation
import com.thefuh.markinbook.routes.schools.disciplines.DisciplinesRepository
import com.thefuh.markinbook.routes.schools.groups.GroupsRepository
import com.thefuh.markinbook.routes.schools.students.StudentsRepository
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.HomeworksRepository
import com.thefuh.markinbook.routes.schools.teachers.TeachersRepository
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
    homeworksRepository: HomeworksRepository,
    groupsRepository: GroupsRepository,
    disciplinesRepository: DisciplinesRepository,
) {
    authenticate {
        withAnyRole(Role.STUDENT, Role.TEACHER) {
            get<LessonsLocation.ByDays> { byDays ->
                val userPrincipal = call.principal<UserPrincipal>()!!

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
                    val allLessonsInWeek = if (userPrincipal.role == Role.STUDENT) {
                        val student = studentsRepository.getById(userPrincipal.userId)!!
                        lessonsRepository.getAllForWeekForGroup(
                            student.groupId.value,
                            mondayStartMillis,
                            nextMondayStartMillis
                        )
                            .toStudentLessons(userPrincipal.userId)
                    } else {
                        lessonsRepository.getAllForWeekForTeacher(
                            userPrincipal.userId,
                            mondayStartMillis,
                            nextMondayStartMillis
                        )
                            .toTeacherLessons()
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
            get<LessonsLocation.Lesson> { getLesson ->
                val userPrincipal = call.principal<UserPrincipal>()!!

                val lesson = dbQuery {
                    val rawLesson = lessonsRepository.getById(getLesson.lessonId)
                    when (userPrincipal.role) {
                        Role.STUDENT -> {
                            rawLesson?.toStudentLesson(userPrincipal.userId)
                        }
                        Role.TEACHER -> {
                            rawLesson?.toTeacherLesson()
                        }
                        else -> {
                            null
                        }
                    }
                }
                call.respond(HttpStatusCode.OK, lesson!!)
            }
        }
        withRole(Role.TEACHER) {
            post<LessonsLocation.Add> {
                val userId = call.principal<UserPrincipal>()?.userId!!

                val teacherEntity = dbQuery { teachersRepository.getById(userId)!! }

                val parameters = call.receiveParameters()

                val groupId = parameters[LessonsLocation.Add.ARG_GROUP_ID]?.toIntOrNull()!!
                val groupEntity = dbQuery { groupsRepository.getById(groupId) }!!

                val disciplineId = parameters[LessonsLocation.Add.ARG_DISCIPLINE_ID]?.toIntOrNull()!!
                val disciplineEntity = dbQuery { disciplinesRepository.getById(disciplineId) }!!

                val start = parameters[LessonsLocation.Add.ARG_START]?.toLongOrNull()!!

                val durationInMinutes = parameters[LessonsLocation.Add.ARG_DURATION_IN_MINUTES]?.toIntOrNull()!!

                val homeworkEntity = dbQuery { homeworksRepository.add() }
                val addedLesson = dbQuery {
                    lessonsRepository.add(
                        teacherEntity,
                        groupEntity,
                        disciplineEntity,
                        start,
                        durationInMinutes,
                        homeworkEntity
                    ).toTeacherLesson()
                }
                call.respond(HttpStatusCode.OK, addedLesson)
            }
        }
    }
}