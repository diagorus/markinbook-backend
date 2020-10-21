package com.thefuh.markinbook.auth

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*

class AuthorizationException(override val message: String) : Exception(message)

class RoleBasedAuthorization(config: Configuration) {
    private val getRoles = config.getRolesBlock

    class Configuration {
        internal var getRolesBlock: (Principal) -> Role? = { null }

        fun getRoles(gr: (Principal) -> Role?) {
            getRolesBlock = gr
        }
    }

    fun interceptPipeline(
        pipeline: ApplicationCallPipeline,
        any: Set<Role>? = null,
        all: Set<Role>? = null,
        none: Set<Role>? = null
    ) {
        pipeline.insertPhaseAfter(ApplicationCallPipeline.Features, Authentication.ChallengePhase)
        pipeline.insertPhaseAfter(Authentication.ChallengePhase, AuthorizationPhase)

        pipeline.intercept(AuthorizationPhase) {
            val principal =
                call.authentication.principal<Principal>() ?: throw AuthorizationException("Missing principal")
            val role = getRoles(principal)
            val denyReasons = mutableListOf<String>()
            all?.let {
                val missing = all - role
                if (missing.isNotEmpty()) {
                    denyReasons += "Principal $principal lacks required role(s) $role"
                }
            }
            any?.let {
                if (any.none { it == role }) {
                    denyReasons += "Principal $principal has none of the sufficient role(s) $role"
                }
            }
            none?.let {
                if (none.any { it == role }) {
                    denyReasons += "Principal $principal has forbidden role(s) $role"
                }
            }
            if (denyReasons.isNotEmpty()) {
                val message = denyReasons.joinToString(". ")
//                logger.warn { "Authorization failed for ${call.request.path()}. ${message}" }
                throw AuthorizationException(message)
            }
        }
    }


    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Configuration, RoleBasedAuthorization> {
        override val key = AttributeKey<RoleBasedAuthorization>("RoleBasedAuthorization")

        val AuthorizationPhase = PipelinePhase("Authorization")

        override fun install(
            pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit
        ): RoleBasedAuthorization {
            val configuration = Configuration().apply(configure)
            return RoleBasedAuthorization(configuration)
        }
    }
}

class AuthorizedRouteSelector(private val description: String) : RouteSelector(RouteSelectorEvaluation.qualityConstant) {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Constant
    override fun toString(): String = "(authorize ${description})"
}

fun Route.withRole(role: Role, build: Route.() -> Unit) = authorizedRoute(all = setOf(role), build = build)

fun Route.withAllRoles(vararg roles: Role, build: Route.() -> Unit) =
    authorizedRoute(all = roles.toSet(), build = build)

fun Route.withAnyRole(vararg roles: Role, build: Route.() -> Unit) = authorizedRoute(any = roles.toSet(), build = build)

fun Route.withoutRoles(vararg roles: Role, build: Route.() -> Unit) =
    authorizedRoute(none = roles.toSet(), build = build)

private fun Route.authorizedRoute(
    any: Set<Role>? = null,
    all: Set<Role>? = null,
    none: Set<Role>? = null, build: Route.() -> Unit
): Route {
    val description = listOfNotNull(
        any?.let { "anyOf (${any.joinToString(" ")})" },
        all?.let { "allOf (${all.joinToString(" ")})" },
        none?.let { "noneOf (${none.joinToString(" ")})" }
    ).joinToString(",")
    val authorizedRoute = createChild(AuthorizedRouteSelector(description))
    application.feature(RoleBasedAuthorization).interceptPipeline(authorizedRoute, any, all, none)
    authorizedRoute.build()
    return authorizedRoute
}