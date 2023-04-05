package com.michael.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

fun Application.configureSecurity() {
    val realm = environment.config.property("jwt.realm").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val secret = String(Base64.getDecoder().decode(environment.config.property("jwt.secret").getString()))
    authentication {
        jwt(realm) {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(jwtAudience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                if(Date(System.currentTimeMillis()).after(credential.expiresAt)){
                     null
                }else {
                    if (credential.payload.getClaim("uid").asInt() != 0) JWTPrincipal(credential.payload) else null
                }
            }
        }
    }
}
