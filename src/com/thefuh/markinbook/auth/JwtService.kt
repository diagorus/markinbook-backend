package com.thefuh.markinbook.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.thefuh.markinbook.data.User
import java.util.*

class JwtService {

    private val issuer = "markinbook-backend"
    private val jwtSecret = System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", user.id)
        .withExpiresAt(expiresAt())
        .sign(algorithm)

    private fun expiresAt() = 
        Date(System.currentTimeMillis() + 3_600_000 * 24) // 24 hours
}