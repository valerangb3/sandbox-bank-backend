package com.example.routing

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.config.JwtConfig
import com.example.domain.model.LoginUser
import com.example.domain.model.RegisterUser
import com.example.repository.UserRepository
import com.example.routing.model.request.RefreshRequest
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
import routing.model.response.JwtTokensResponse
import routing.model.response.ServerResponse
import service.JwtService
import java.util.Date

fun Application.configureAuthRouting(
    repository: UserRepository,
    jwtService: JwtService,

) {


    routing {
        route("/api/v1") {
            post("/auth") {
                val userRequest = call.receive<LoginUser>()
                val user = repository.userByLogin(userRequest.login)
                user?.let { curUser ->
                    val isEquals = repository.isPasswordEquals(userRequest.password, curUser)
                    if (isEquals) {
                        val (accessToken, refreshToken) = jwtService.genTokens(userRequest)
                        val response = ServerResponse(
                            status = 201,
                            message = "success",
                            data = JwtTokensResponse(accessToken = accessToken, refreshToken = refreshToken)
                        )
                        call.respond(response)
                    } else {
                        ServerResponse(
                            status = 202,
                            message = "error",
                            data = "invalid password"
                        )
                    }
                } ?: call.respond(
                    ServerResponse(
                        status = 401,
                        message = "error",
                        data = "user not exist"
                    )
                )
            }
            post("/refresh") {
                //TODO доделать рефреш токена
                val refreshRequest = call.receive<RefreshRequest>()

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