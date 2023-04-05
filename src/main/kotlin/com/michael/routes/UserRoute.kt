package com.michael.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.michael.entities.User
import com.michael.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Application.configureUserRouting() {
    val userService: UserService by inject()

    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val realm = environment.config.property("jwt.realm").getString()
    val secret = String(Base64.getDecoder().decode(environment.config.property("jwt.secret").getString()))
    val algorithm = Algorithm.HMAC256(secret)

    routing {
        route("/user"){
            authenticate(realm) {
                get(){
                    val principal = call.principal<JWTPrincipal>()
                    val uid = principal!!.payload.getClaim("uid").asInt()
                    val user = userService.getUserByUid(uid!!)
                    if(user != null) {
                        call.respond(mapOf("username" to user.username))
                    }
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            post("/login"){
                val loginUser = call.receive<User>()
                val user = userService.getUserByEmail(loginUser.email)
                if(user?.password != loginUser.password) {
                    call.respond(HttpStatusCode.Forbidden, "wrong password")
                }
                val expireTime = Date(System.currentTimeMillis() + 60000)
                val token = JWT.create()
                    .withClaim("uid", user!!.uid)
                    .withAudience(audience).withIssuer(issuer)
                    .withClaim("uid", user.uid).withExpiresAt(expireTime)
                    .sign(algorithm)
                call.respond(mapOf("access_token" to token, "expires_at" to expireTime))
            }
            post("/new"){
                val user = call.receive<User>()
                if(user.email == "" || !user.email.contains("@")){
                    call.respond(HttpStatusCode.BadRequest, "invalid email or password")
                }
                if(user.password.length < 6 || user.password.length > 32){
                    call.respond(HttpStatusCode.BadRequest, "invalid email or password")
                }
                userService.newUser(user)
                call.respondText("ok")
            }
        }
    }
}