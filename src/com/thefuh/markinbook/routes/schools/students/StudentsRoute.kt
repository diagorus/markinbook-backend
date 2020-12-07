package com.thefuh.markinbook.routes.schools.students

import com.thefuh.markinbook.auth.UserPrincipal
import com.thefuh.markinbook.DatabaseFactory.dbQuery
import com.thefuh.markinbook.routes.schools.StudentsLocation
import com.thefuh.markinbook.utils.copyToSuspend
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import java.io.File

@KtorExperimentalLocationsAPI
fun Route.students(
    studentsRepository: StudentsRepository,
    uploadDir: File
) {
    authenticate {
        get<StudentsLocation> {
            val allStudents = dbQuery { studentsRepository.getAll().toStudents() }
            call.respond(HttpStatusCode.OK, allStudents)
        }
        get<StudentsLocation.Student> { student ->
            val foundStudent = dbQuery { studentsRepository.getById(student.studentId)?.toStudent() }
            if (foundStudent == null) {
                //todo
            } else {
                call.respond(HttpStatusCode.OK, foundStudent)
            }
        }
        get<StudentsLocation.Current> {
            val userId = call.principal<UserPrincipal>()?.userId
            if (userId == null) {
                //todo
                return@get
            }
            val currentStudent = dbQuery { studentsRepository.getById(userId)?.toStudent() }
            if (currentStudent == null) {
                //todo
                return@get
            } else {
                call.respond(HttpStatusCode.OK, currentStudent)
            }
        }
        post<StudentsLocation.Current.AddProfileImage> {
            val userId = call.principal<UserPrincipal>()?.userId!!

            val multipart = call.receiveMultipart()
            var title = ""
            var rawImageFile: File? = null

            // Processes each part of the multipart input content of the user
            multipart.forEachPart { part ->
                if (part is PartData.FormItem) {
                    if (part.name == "title") {
                        title = part.value
                    }
                } else if (part is PartData.FileItem) {
                    val ext = File(part.originalFileName).extension
                    val file = File(
                        uploadDir,
                        "upload-${System.currentTimeMillis()}-${userId.hashCode()}-${title.hashCode()}.$ext"
                    )

                    part.streamProvider().use { its -> file.outputStream().buffered().use { its.copyToSuspend(it) } }
                    rawImageFile = file
                }

                part.dispose()
            }

            val imageUrl = "http://localhost:8081/${rawImageFile!!.path}"

            val updatedStudent = dbQuery { studentsRepository.updateImage(userId, imageUrl)?.toStudent() }!!
            call.respond(HttpStatusCode.OK, updatedStudent)
        }
    }
}