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
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

const val API_NAME = "api"
const val API_VERSION = "v1"

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

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

    DatabaseFactory.init()

    val jwtService = JwtService()

    val usersRepository = UsersRepository()

    install(Authentication) {
        session<UserSession> {
            validate {
                val user = usersRepository.findById(it.userId)?.toUser()
                user
            }
        }
        jwt("jwt") {
            verifier(jwtService.verifier)
            realm = "markinbook-backend"
            validate {
                val payload = it.payload
                val userId = payload.getClaim("id").asInt()
                val user = usersRepository.findById(userId)?.toUser()
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
        users(studentsRepository, schoolRepository, groupsRepository, usersRepository, jwtService, ::hash)
        schools(schoolRepository)
        disciplines(schoolRepository, disciplineRepository)
        students(studentsRepository)
        lessons(studentsRepository,lessonsRepository, groupsRepository, disciplineRepository)
        homeworks(lessonsRepository, homeworksRepository)
//        groups
//        tasks
    }
}