package com.michael.plugins

import com.michael.routes.configHealthRoute
import com.michael.routes.configureUserRouting
import io.ktor.server.application.*

fun Application.configureRouting() {
    configHealthRoute()
    configureUserRouting()
}
