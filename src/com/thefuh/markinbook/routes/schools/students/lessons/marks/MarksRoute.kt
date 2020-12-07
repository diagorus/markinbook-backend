package com.thefuh.markinbook.routes.schools.students.lessons.marks

import com.thefuh.markinbook.DatabaseFactory.dbQuery
import com.thefuh.markinbook.auth.Role
import com.thefuh.markinbook.auth.withRole
import com.thefuh.markinbook.routes.schools.HomeworksLocation
import com.thefuh.markinbook.routes.schools.LessonsLocation.Lesson.Marks
import com.thefuh.markinbook.routes.schools.students.StudentsRepository
import com.thefuh.markinbook.routes.schools.students.lessons.LessonsRepository
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.HomeworksRepository
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
fun Route.marks(
    lessonsRepository: LessonsRepository,
    homeworksRepository: HomeworksRepository,
    studentsRepository: StudentsRepository,
    marksRepository: MarksRepository
) {
    authenticate {
        withRole(Role.TEACHER) {
            post<Marks.Add> { postMarksAdd ->
                val lesson = dbQuery { lessonsRepository.getById(postMarksAdd.marks.lesson.lessonId)!! }

                val parameters = call.receive<Parameters>()
                val studentId = parameters[Marks.Add.ARG_STUDENT_ID]?.toIntOrNull()!!
                val student = dbQuery { studentsRepository.getById(studentId)!! }

                val value = parameters[Marks.Add.ARG_VALUE]?.toIntOrNull()!!

                val newMark = dbQuery { marksRepository.add(student, lesson, null, value).toTeacherMark() }
                call.respond(HttpStatusCode.OK, newMark)
            }
            post<HomeworksLocation.Homework.Marks.Add> { postMarksAdd ->
                val homework = dbQuery { homeworksRepository.getById(postMarksAdd.marks.homework.homeworkId)!! }

                val parameters = call.receive<Parameters>()
                val studentId = parameters[Marks.Add.ARG_STUDENT_ID]?.toIntOrNull()!!
                val student = dbQuery { studentsRepository.getById(studentId)!! }

                val value = parameters[Marks.Add.ARG_VALUE]?.toIntOrNull()!!

                val newMark = dbQuery { marksRepository.add(student, null, homework, value).toTeacherMark() }
                call.respond(HttpStatusCode.OK, newMark)
            }
        }
    }
}