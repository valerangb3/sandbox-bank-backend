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
import service.AuthError
import service.AuthResult
import service.AuthService
import service.JwtService

fun Application.configureAuthRouting(
    repository: UserRepository,
    authService: AuthService
) {


    routing {
        route("/api/v1") {
            post("/auth") {
                val userRequest = call.receive<LoginUser>()
                val result = authService.authenticate(userRequest)
                val response = when (result) {
                    is AuthResult.Success -> {
                        val (accessToken, refreshToken) = result.tokes
                        ServerResponse(
                            status = 201,
                            message = "success",
                            data = JwtTokensResponse(accessToken = accessToken, refreshToken = refreshToken)
                        )
                    }
                    is AuthResult.Error -> {
                        when (result.authError) {
                            AuthError.INVALID_PASSWORD -> {
                                ServerResponse(
                                    status = 202,
                                    message = "error",
                                    data = "invalid password"
                                )
                            }
                            AuthError.USER_NOT_EXIST -> {
                                ServerResponse(
                                    status = 401,
                                    message = "error",
                                    data = "user not exist"
                                )
                            }
                        }
                    }
                }
                call.respond(response)
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