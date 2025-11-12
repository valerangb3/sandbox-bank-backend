package com.example.config

import io.ktor.server.config.ApplicationConfig

data class JwtConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String
) {
    constructor(config: ApplicationConfig) : this(
        secret = config.property("jwt.secret").getString(),
        issuer = config.property("jwt.issuer").getString(),
        audience = config.property("jwt.audience").getString(),
        realm = config.property("jwt.realm").getString()
    )
}