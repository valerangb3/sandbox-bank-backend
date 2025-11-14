plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}
tasks.named<JavaExec>("run") {
    val envFile = file(".env")
    if (envFile.exists()) {
        logger.lifecycle("Gradle: Loading environment variables from .env file")
        envFile.readLines().forEach { line ->
            if (line.isNotEmpty() && !line.startsWith("#")) {
                val equalsPos = line.indexOf("=")
                if (equalsPos > 0) {
                    val key = line.substring(0, equalsPos).trim()
                    val value = line.substring(equalsPos + 1).trim()
                    environment(key, value)
                }
            }
        }
    } else {
        logger.warn("WARNING: .env file not found at ${envFile.absolutePath}")
    }
}
dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.postgresql)
    // implementation(libs.h2)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.ktor.client.content.negotiation)
    implementation(libs.exposed.dao)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.dotenv.kotlin)
    implementation(libs.exposed.migration.core)
    implementation(libs.exposed.migration.jdbc)
    implementation(libs.flyway)
    implementation("com.zaxxer:HikariCP:5.1.0")
}

