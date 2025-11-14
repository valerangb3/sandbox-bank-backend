@file:OptIn(ExperimentalDatabaseMigrationApi::class)

package org.example

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import repository.db.UserTable
import org.jetbrains.exposed.v1.core.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils
import javax.sql.DataSource

const val MIGRATIONS_DIRECTORY = "src/main/resources/db/migration" // Location of migration scripts

private const val URL = "jdbc:postgresql://localhost:5432/vgb3"
private const val USER = "vgb3"
private const val PASSWORD = "qwerty"


fun createPostgresDataSource(): DataSource {
    val config = HikariConfig()
    config.jdbcUrl = URL
    config.username = USER
    config.password = PASSWORD
    config.maximumPoolSize = 10
    return HikariDataSource(config)
}

fun main() {
    val postgresDataSource = createPostgresDataSource()
    val flyway = Flyway.configure()
        .dataSource(URL, USER, PASSWORD)
        //.driver("org.postgresql.Driver")
        .locations("classpath:db/migration")
        .baselineOnMigrate(true)
        .schemas("public")
        .load()

    val h2db = Database.connect(
        postgresDataSource
    )
    //simulateExistingDatabase(h2db)

    transaction(h2db) {
        //generateMigrationScript()
        flyway.migrate()
    }
}

fun simulateExistingDatabase(database: Database) {
    transaction(database) {
        exec("DROP TABLE IF EXISTS task CASCADE")
        exec("DROP TABLE IF EXISTS users CASCADE")
    }
}

fun generateMigrationScript() {
    // Generate a migration script in the specified path
    MigrationUtils.generateMigrationScript(
        UserTable,
        scriptDirectory = MIGRATIONS_DIRECTORY,
        scriptName = "V1__create_user",
        withLogs = true
    )
}