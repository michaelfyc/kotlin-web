package com.michael

import com.michael.models.UserDAO
import com.michael.plugins.*
import com.michael.services.UserService
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.ktorm.database.Database

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

fun buildDatabase(config: ApplicationConfig): Database {
    val hikariConfig = HikariConfig()
    hikariConfig.jdbcUrl = config.propertyOrNull("url")?.getString() ?: ""
    hikariConfig.username = config.propertyOrNull("username")?.getString() ?: ""
    hikariConfig.password = config.propertyOrNull("password")?.getString() ?: ""
    hikariConfig.setDriverClassName(config.propertyOrNull("driver")?.getString() ?: "")
    return Database.connect(HikariDataSource(hikariConfig))
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureRouting()
    val databaseConfig = ApplicationConfig("application.yaml").config("database")
    install(Koin) {
        modules(
            module {
                single { buildDatabase(databaseConfig) }
                singleOf(::UserDAO)
                singleOf(::UserService)
            },
        )
    }
}
