package com.thefuh.markinbook

import com.thefuh.markinbook.auth.JwtService
import com.thefuh.markinbook.auth.UserSession
import com.thefuh.markinbook.auth.RoleBasedAuthorization
import com.thefuh.markinbook.auth.hash
import com.thefuh.markinbook.data.User
import com.thefuh.markinbook.database.DatabaseFactory
import com.thefuh.markinbook.database.tables.schools.disciplines.DisciplinesRepository
import com.thefuh.markinbook.database.tables.schools.SchoolsRepository
import com.thefuh.markinbook.database.tables.schools.students.StudentsRepository
import com.thefuh.markinbook.database.tables.schools.students.groups.GroupsRepository
import com.thefuh.markinbook.database.tables.schools.students.homeworks.HomeworksRepository
import com.thefuh.markinbook.database.tables.schools.students.lessons.LessonsRepository
import com.thefuh.markinbook.database.tables.users.UsersRepository
import com.thefuh.markinbook.routes.schools.disciplines.disciplines
import com.thefuh.markinbook.routes.schools.groups.groups
import com.thefuh.markinbook.routes.schools.schools
import com.thefuh.markinbook.routes.schools.students.homeworks.homeworks
import com.thefuh.markinbook.routes.schools.students.lessons.lessons
import com.thefuh.markinbook.routes.schools.students.students
import com.thefuh.markinbook.routes.users.toUser
import com.thefuh.markinbook.routes.users.users
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.locations.*
import io.ktor.sessions.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

const val API_NAME = "api"
const val API_VERSION = "v1"

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.module() {
    install(ContentNegotiation) {
        json(
            Json(DefaultJson) {
                prettyPrint = true
            }
        )
    }
    install(CallLogging) {
        level = Level.INFO
    }
    install(Sessions) {
        header<UserSession>("Authorization")
    }
    install(Locations)
    install(RoleBasedAuthorization) {
        getRoles { (it as User).role }
    }
    install(CORS) {
        anyHost()
    }
    DatabaseFactory.init()

    val jwtService = JwtService()

    val usersRepository = UsersRepository()

    install(Authentication) {
        session<UserSession> {
            validate {
                val user = usersRepository.getById(it.userId)?.toUser()
                user
            }
        }
        jwt("jwt") {
            verifier(jwtService.verifier)
            realm = "markinbook-backend"
            validate {
                val payload = it.payload
                val userId = payload.getClaim("id").asInt()
                val user = usersRepository.getById(userId)?.toUser()
                user
            }
        }
    }

    val schoolRepository = SchoolsRepository()
    val disciplineRepository = DisciplinesRepository()

    val studentsRepository = StudentsRepository()
    val lessonsRepository = LessonsRepository()
    val groupsRepository = GroupsRepository()
    val homeworksRepository = HomeworksRepository()

    routing {
//       trace { application.log.trace(it.buildText()) }

        schools(schoolRepository)
        users(studentsRepository, schoolRepository, groupsRepository, usersRepository, jwtService, ::hash)
        disciplines(schoolRepository, disciplineRepository)
        students(studentsRepository)
        lessons(studentsRepository, lessonsRepository, groupsRepository, disciplineRepository)
        homeworks(lessonsRepository, homeworksRepository)
        groups(schoolRepository, groupsRepository)
//        tasks
    }

    val root = feature(Routing)
    val allRoutes = allRoutes(root)
    val allRoutesWithMethod = allRoutes.filter { it.selector is HttpMethodRouteSelector }
    allRoutesWithMethod.forEach {
        log.info("route: $it")
    }
}


fun allRoutes(root: Route): List<Route> {
    return listOf(root) + root.children.flatMap { allRoutes(it) }
}