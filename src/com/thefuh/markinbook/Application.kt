package com.thefuh.markinbook

import com.thefuh.markinbook.auth.JwtService
import com.thefuh.markinbook.auth.UserPrincipal
import com.thefuh.markinbook.auth.RoleBasedAuthorization
import com.thefuh.markinbook.auth.hash
import com.thefuh.markinbook.DatabaseFactory.dbQuery
import com.thefuh.markinbook.routes.schools.disciplines.DisciplinesRepository
import com.thefuh.markinbook.routes.schools.SchoolsRepository
import com.thefuh.markinbook.routes.schools.students.StudentsRepository
import com.thefuh.markinbook.routes.schools.groups.GroupsRepository
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.HomeworksRepository
import com.thefuh.markinbook.routes.schools.students.lessons.LessonsRepository
import com.thefuh.markinbook.routes.schools.teachers.TeachersRepository
import com.thefuh.markinbook.routes.users.UsersRepository
import com.thefuh.markinbook.routes.FileLocation
import com.thefuh.markinbook.routes.schools.disciplines.disciplines
import com.thefuh.markinbook.routes.schools.groups.groups
import com.thefuh.markinbook.routes.schools.schools
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.homeworks
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.tasks.TasksRepository
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.tasks.tasks
import com.thefuh.markinbook.routes.schools.students.lessons.lessons
import com.thefuh.markinbook.routes.schools.students.lessons.marks.MarksRepository
import com.thefuh.markinbook.routes.schools.students.lessons.marks.marks
import com.thefuh.markinbook.routes.schools.students.students
import com.thefuh.markinbook.routes.schools.teachers.teachers
import com.thefuh.markinbook.routes.users.toUser
import com.thefuh.markinbook.routes.users.users
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import org.slf4j.event.Level
import java.io.File
import java.io.IOException
import com.google.firebase.FirebaseApp

import com.google.auth.oauth2.GoogleCredentials

import com.google.firebase.FirebaseOptions
import com.thefuh.markinbook.routes.users.tokens.PushTokensRepository
import com.thefuh.markinbook.routes.users.tokens.pushTokens
import com.thefuh.markinbook.utils.push.PushManager

const val API_NAME = "api"
const val API_VERSION = "v1"

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.module() {
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
        .build()
    FirebaseApp.initializeApp(options)

    install(CallLogging) {
        level = Level.INFO
    }
//    install(DefaultHeaders)
    install(ContentNegotiation) {
        json(
            Json(DefaultJson) {
                prettyPrint = true
            }
        )
    }
    install(Locations)
    install(CORS) {
        header(HttpHeaders.Authorization)
        header(HttpHeaders.AccessControlAllowOrigin)
        anyHost()
    }

    DatabaseFactory.init()

    val jwtService = JwtService()

    val usersRepository = UsersRepository()

    install(RoleBasedAuthorization) {
        getRoles { (it as UserPrincipal).role }
    }
    install(Authentication) {
        jwt {
            verifier(jwtService.verifier)
            realm = "markinbook-backend"
            validate {
                val payload = it.payload
                val userId = payload.getClaim("id").asInt()
                val user = dbQuery { usersRepository.getById(userId)?.toUser() }
                user?.let { UserPrincipal(it.id, it.role) }
            }
        }
    }

    val markinbookConfig = environment.config.config("markinbook-backend")
    val uploadDirPath: String = markinbookConfig.property("upload.dir").getString()
    val uploadDir = File(uploadDirPath)
    if (!uploadDir.mkdirs() && !uploadDir.exists()) {
        throw IOException("Failed to create directory ${uploadDir.absolutePath}")
    }

    val pushTokensRepository = PushTokensRepository()

    val schoolRepository = SchoolsRepository()
    val disciplineRepository = DisciplinesRepository()

    val studentsRepository = StudentsRepository()
    val teachersRepository = TeachersRepository()
    val lessonsRepository = LessonsRepository()
    val groupsRepository = GroupsRepository()
    val homeworksRepository = HomeworksRepository()
    val tasksRepository = TasksRepository()
    val marksRepository = MarksRepository()

    val pushManager = PushManager(pushTokensRepository, groupsRepository, log)

    routing {
//       trace { application.log.trace(it.buildText()) }

        schools(schoolRepository)
        users(
            studentsRepository,
            teachersRepository,
            schoolRepository,
            groupsRepository,
            usersRepository,
            jwtService,
            ::hash
        )
        disciplines(schoolRepository, disciplineRepository)
        students(studentsRepository, uploadDir)
        teachers(teachersRepository)
        lessons(studentsRepository, teachersRepository, lessonsRepository, groupsRepository, disciplineRepository)
        homeworks(lessonsRepository, homeworksRepository, tasksRepository, pushManager)
        tasks(homeworksRepository, tasksRepository)
        groups(schoolRepository, groupsRepository)
        marks(lessonsRepository, homeworksRepository, studentsRepository, marksRepository, pushManager)
        pushTokens(usersRepository, pushTokensRepository)
        get<FileLocation> { fileLocationGet ->
            val file = File(fileLocationGet.filePath.joinToString(separator = "/"))
            if (file.exists()) {
                call.respondFile(file)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }

//    val root = feature(Routing)
//    val allRoutes = allRoutes(root)
//    val allRoutesWithMethod = allRoutes.filter { it.selector is HttpMethodRouteSelector }
//    allRoutesWithMethod.forEach {
//        log.info("route: $it")
//    }
}


fun allRoutes(root: Route): List<Route> {
    return listOf(root) + root.children.flatMap { allRoutes(it) }
}