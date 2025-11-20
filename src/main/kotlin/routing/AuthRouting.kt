package com.example.routing

import com.example.domain.model.LoginUser
import com.example.domain.model.RegisterUser
import com.example.repository.UserRepository
import com.example.routing.model.request.RefreshRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.util.reflect.typeInfo
import kotlinx.serialization.serializer
import routing.model.response.JwtTokensResponse
import routing.model.response.ServerResponse
import service.AuthError
import service.AuthResult
import service.AuthService

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
                            description = "User authenticate",
                            data = JwtTokensResponse(accessToken = accessToken, refreshToken = refreshToken)
                        )
                    }

                    is AuthResult.Error -> {
                        when (result.authError) {
                            AuthError.INVALID_PASSWORD -> {
                                ServerResponse(
                                    status = 202,
                                    message = "error",
                                    description = "Invalid password",
                                    data = null
                                )
                            }

                            AuthError.USER_NOT_EXIST -> {
                                ServerResponse(
                                    status = 401,
                                    message = "error",
                                    description = "User not exist",
                                    data = null
                                )
                            }
                        }
                    }
                }
                call.respond(response)
            }
            post("/refresh") {
                val tokensRequest = call.receive<RefreshRequest>()
                val refreshToken = tokensRequest.refreshToken
                val result = authService.refreshTokens(refreshToken)
                val response = when (result) {
                    is AuthResult.Error -> {
                        ServerResponse(
                            status = 401,
                            message = "error",
                            description = "User not exist",
                            data = null
                        )
                    }

                    is AuthResult.Success -> {
                        val (accessToken, refreshToken) = result.tokes
                        ServerResponse(
                            status = 201,
                            message = "success",
                            description = "User create",
                            data = JwtTokensResponse(accessToken = accessToken, refreshToken = refreshToken)
                        )
                    }
                }
                call.respond(response)
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