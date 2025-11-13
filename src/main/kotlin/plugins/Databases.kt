package com.example.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.v1.jdbc.Database

fun Application.configureDatabases() {
    Database.connect(
        "jdbc:postgresql://localhost:5432/vgb3",
        user = "vgb3",
        password = "qwerty"
    )
}