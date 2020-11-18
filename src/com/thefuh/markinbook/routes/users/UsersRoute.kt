package com.thefuh.markinbook.routes.users

import com.thefuh.markinbook.auth.JwtService
import com.thefuh.markinbook.auth.Role
import com.thefuh.markinbook.auth.UserSession
import com.thefuh.markinbook.database.DatabaseFactory.dbQuery
import com.thefuh.markinbook.database.tables.schools.SchoolsRepository
import com.thefuh.markinbook.database.tables.schools.students.StudentsRepository
import com.thefuh.markinbook.database.tables.schools.students.groups.GroupsRepository
import com.thefuh.markinbook.database.tables.users.UsersRepository
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@KtorExperimentalLocationsAPI
fun Route.users(
    studentsRepository: StudentsRepository,
    schoolsRepository: SchoolsRepository,
    groupsRepository: GroupsRepository,
    usersRepository: UsersRepository,
    jwtService: JwtService,
    hashFunction: (String) -> String,
) {
    post<UsersLocation.SignUp> {
        val parameters = call.receive<Parameters>()

        val firstName = parameters[UsersLocation.SignUp.FIRST_NAME]
            ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "Missing Fields"
            )
        val lastName = parameters[UsersLocation.SignUp.LAST_NAME]
            ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "Missing Fields"
            )
        val schoolId = parameters[UsersLocation.SignUp.SCHOOL_ID]?.toIntOrNull()
            ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "Missing Fields"
            )

        val schoolEntity = dbQuery { schoolsRepository.getById(schoolId) }
            ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "School not found"
            )
        val groupId = parameters[UsersLocation.SignUp.GROUP_ID]?.toIntOrNull()
            ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "Missing Fields"
            )
        val groupEntity = dbQuery { groupsRepository.getById(groupId) }
            ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "Group not found"
            )

        val email = parameters[UsersLocation.SignUp.EMAIL]
            ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "Missing Fields"
            )
        val password = parameters[UsersLocation.SignUp.PASSWORD]
            ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "Missing Fields"
            )
        val role = parameters[UsersLocation.SignUp.ROLE]?.let { Role.valueOf(it) }
            ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "Missing Fields"
            )

        val passwordHash = hashFunction(password)
        try {
            val newUser = dbQuery { usersRepository.add(email, passwordHash, role).toUser() }

            dbQuery { studentsRepository.add(newUser.id, firstName, lastName, schoolEntity, groupEntity) }

            call.sessions.set(UserSession(newUser.id))
            call.respondText(
                jwtService.generateToken(newUser),
                status = HttpStatusCode.Created
            )
        } catch (e: Throwable) {
            application.log.error("Failed to register user", e)
            call.respond(HttpStatusCode.BadRequest, "Problems creating User")
        }
    }
    post<UsersLocation.SignIn> {
        val parameters = call.receive<Parameters>()
        val email = parameters[UsersLocation.SignIn.EMAIL]
            ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing Fields")
        val password = parameters[UsersLocation.SignIn.PASSWORD]
            ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing Fields")
        val roleString = parameters[UsersLocation.SignIn.ROLE]
            ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing Fields")

        val role = Role.findByTitle(roleString)

        val hash = hashFunction(password)
        try {
            val currentUser = dbQuery { usersRepository.getByEmail(email)?.toUser() }
            if (currentUser == null) {
                //todo
            } else {
                if (currentUser.passwordHash == hash) {
                    call.sessions.set(UserSession(currentUser.id))
                    call.respondText(jwtService.generateToken(currentUser))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
                }
            }
        } catch (e: Throwable) {
            application.log.error("Failed to register user", e)
            call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
        }
    }
    post<UsersLocation.SignOut> {
        //todo
    }
}