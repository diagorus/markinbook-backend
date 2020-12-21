package com.thefuh.markinbook.routes.users.tokens

import com.thefuh.markinbook.DatabaseFactory.dbQuery
import com.thefuh.markinbook.auth.Role
import com.thefuh.markinbook.auth.UserPrincipal
import com.thefuh.markinbook.auth.withRole
import com.thefuh.markinbook.routes.users.UsersLocation
import com.thefuh.markinbook.routes.users.UsersLocation.PushTokens
import com.thefuh.markinbook.routes.users.UsersRepository
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
fun Route.pushTokens(
    usersRepository: UsersRepository,
    pushTokensRepository: PushTokensRepository,
) {
    authenticate {
        withRole(Role.STUDENT) {
            post<PushTokens.Add> {
                val userPrincipal = call.principal<UserPrincipal>()!!
                val token = call.receive<Parameters>()[PushTokens.Add.ARG_TOKEN]!!
                dbQuery {
                    val userEntity = usersRepository.getById(userPrincipal.userId)!!

                    val previousTokens = pushTokensRepository.getByUserId(userPrincipal.userId)
                    previousTokens.forEach { it.delete() }

                    pushTokensRepository.add(userEntity, token)
                }
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}