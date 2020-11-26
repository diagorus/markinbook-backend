package com.thefuh.markinbook.routes.schools.students

import com.thefuh.markinbook.auth.UserSession
import com.thefuh.markinbook.database.DatabaseFactory.dbQuery
import com.thefuh.markinbook.database.tables.students.StudentsRepository
import com.thefuh.markinbook.routes.schools.StudentsLocation
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File
import java.io.InputStream
import java.io.OutputStream

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
            val userId = call.principal<UserSession>()?.userId
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
            val userId = call.principal<UserSession>()?.userId!!

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

/**
 * Utility boilerplate method that suspending,
 * copies a [this] [InputStream] into an [out] [OutputStream] in a separate thread.
 *
 * [bufferSize] and [yieldSize] allows to control how and when the suspending is performed.
 * The [dispatcher] allows to specify where will be this executed (for example a specific thread pool).
 */
suspend fun InputStream.copyToSuspend(
    out: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    yieldSize: Int = 4 * 1024 * 1024,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Long = withContext(dispatcher) {
    val buffer = ByteArray(bufferSize)
    var bytesCopied = 0L
    var bytesAfterYield = 0L
    while (true) {
        val bytes = read(buffer).takeIf { it >= 0 } ?: break
        out.write(buffer, 0, bytes)
        if (bytesAfterYield >= yieldSize) {
            yield()
            bytesAfterYield %= yieldSize
        }
        bytesCopied += bytes
        bytesAfterYield += bytes
    }
    bytesCopied
}