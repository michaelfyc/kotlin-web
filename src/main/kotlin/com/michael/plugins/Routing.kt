package com.michael.plugins

import com.michael.routes.configHealthRoute
import com.michael.routes.configureUserRouting
import io.ktor.server.application.Application

fun Application.configureRouting() {
    configHealthRoute()
    configureUserRouting()
}
