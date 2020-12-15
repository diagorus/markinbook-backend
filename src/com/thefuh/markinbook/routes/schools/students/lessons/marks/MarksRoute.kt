package com.thefuh.markinbook.routes.schools.students.lessons.marks

import com.thefuh.markinbook.DatabaseFactory.dbQuery
import com.thefuh.markinbook.auth.Role
import com.thefuh.markinbook.auth.withRole
import com.thefuh.markinbook.routes.schools.LessonsLocation.Lesson.Homeworks.Homework
import com.thefuh.markinbook.routes.schools.LessonsLocation.Lesson.Marks
import com.thefuh.markinbook.routes.schools.students.StudentsRepository
import com.thefuh.markinbook.routes.schools.students.lessons.LessonsRepository
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.HomeworksRepository
import com.thefuh.markinbook.utils.push.PushManager
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
    marksRepository: MarksRepository,
    pushManager: PushManager,
) {
    authenticate {
        withRole(Role.TEACHER) {
            post<Marks.Add> { postMarksAdd ->
                val lesson = dbQuery { lessonsRepository.getById(postMarksAdd.marks.lesson.lessonId)!! }

                val parameters = call.receive<Parameters>()
                val studentId = parameters[Marks.Add.ARG_STUDENT_ID]?.toIntOrNull()!!
                val student = dbQuery { studentsRepository.getById(studentId)!! }

                val value = parameters[Marks.Add.ARG_VALUE]?.toIntOrNull()!!

                val newMark = dbQuery {
                    val previousMarks = marksRepository.getByStudentIdAndLesson(studentId, lesson)
                    previousMarks.forEach { it.delete() }

                    marksRepository.add(student, lesson, null, value).toTeacherMark()
                }
                val lessonId = dbQuery { lesson.id.value }
                val disciplineTitle = dbQuery { lesson.discipline.title }
                pushManager.pushLessonMark(studentId, lessonId, disciplineTitle)
                call.respond(HttpStatusCode.OK, newMark)
            }
            post<Homework.Marks.Add> { postMarksAdd ->
                val homework = dbQuery { homeworksRepository.getById(postMarksAdd.marks.homework.homeworkId)!! }

                val parameters = call.receive<Parameters>()
                val studentId = parameters[Homework.Marks.Add.ARG_STUDENT_ID]?.toIntOrNull()!!
                val student = dbQuery { studentsRepository.getById(studentId)!! }

                val value = parameters[Homework.Marks.Add.ARG_VALUE]?.toIntOrNull()!!

                val newMark = dbQuery {
                    val previousMarks = marksRepository.getByStudentIdAndHomework(studentId, homework)
                    previousMarks.forEach { it.delete() }

                    marksRepository.add(student, null, homework, value).toTeacherMark()
                }

                val homeworkId = dbQuery { homework.id.value }

                val lessonId = postMarksAdd.marks.homework.homeworks.lesson.lessonId
                val disciplineTitle = dbQuery { lessonsRepository.getById(lessonId)?.discipline?.title }!!
                pushManager.pushHomeworkMark(studentId, homeworkId, disciplineTitle)
                call.respond(HttpStatusCode.OK, newMark)
            }
        }
    }
}