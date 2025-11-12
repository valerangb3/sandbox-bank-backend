package com.example.routing

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.config.JwtConfig
import com.example.domain.model.LoginUser
import com.example.domain.model.RegisterUser
import com.example.repository.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import java.util.Date

fun Application.configureAuthRouting(repository: UserRepository) {


    routing {
        route("/api/v1") {
            post("/auth") {
                val user = call.receive<LoginUser>()
                //TODO check username and password
                val jwtConfig = JwtConfig(environment.config)
                val token = JWT.create()
                    .withAudience(jwtConfig.audience)
                    .withIssuer(jwtConfig.issuer)
                    .withClaim("login", user.login)
                    .withExpiresAt(Date(System.currentTimeMillis() + 60_000))
                    .sign(Algorithm.HMAC256(jwtConfig.secret))

                call.respond(hashMapOf("token" to token))

            }
            post("/registration") {
                val registerUser = call.receive<RegisterUser>()
                if (repository.create(registerUser)) {
                    call.respond(HttpStatusCode.Created)
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}