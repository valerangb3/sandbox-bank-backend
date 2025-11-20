package com.example

import com.example.plugins.configureDatabases
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.repository.PostgresTaskRepository
import com.example.repository.UserRepositoryImpl
import com.example.routing.configureAuthRouting
import com.example.routing.configureRouting
import io.github.cdimascio.dotenv.DotenvEntry
import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.cio.EngineMain
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import repository.JwtTokenRepositoryImpl
import service.AuthService
import service.JwtService

/*fun main(args: Array<String>) {
    val dotenv = dotenv {
        ignoreIfMissing = false // не падаем, если .env нет (например, в проде)
    }
    dotenv.entries().forEach { System.setProperty(it.key, it.value) }
    io.ktor.server.netty.EngineMain.main(args)
}*/

fun main(args: Array<String>) {
    val dotenv = dotenv {
        ignoreIfMissing = false
    }
    dotenv.entries().forEach { System.setProperty(it.key, it.value) }

    // Измените вызов запуска:
    EngineMain.main(args)
}

fun Application.module() {

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    //val repository = FakeTaskRepository()
    val repository = PostgresTaskRepository()
    val userRepository = UserRepositoryImpl()
    val jwtService = JwtService(environment)
    val tokenRepository = JwtTokenRepositoryImpl(userRepository)
    val authService = AuthService(userRepository, tokenRepository, jwtService)

    configureSerialization()
    configureDatabases()
    configureSecurity()
    configureAuthRouting(userRepository, authService)
    configureRouting(repository)
}
