package com.michael.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configHealthRoute(){
    routing {
        get("/health"){
            call.respondText("ok")
        }
    }
}