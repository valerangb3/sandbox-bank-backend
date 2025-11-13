@file:OptIn(ExperimentalDatabaseMigrationApi::class)

package org.example

import com.example.repository.db.TaskTable
import repository.db.UserTable
import org.jetbrains.exposed.v1.core.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils

const val MIGRATIONS_DIRECTORY = "src/main/kotlin/migrations" // Location of migration scripts

val h2db = Database.connect(
    "jdbc:postgresql://localhost:5432/vgb3",
    user = "vgb3",
    password = "qwerty"
)

fun main() {
    simulateExistingDatabase(h2db)

    transaction(h2db) {
        generateMigrationScript()
    }
}

fun simulateExistingDatabase(database: Database) {
    transaction(database) {
        exec("DROP TABLE IF EXISTS task CASCADE")
        exec("CREATE TABLE task(id SERIAL PRIMARY KEY, name VARCHAR(50), description VARCHAR(50), priority VARCHAR(50))")

        exec("INSERT INTO task (name, description, priority) VALUES ('cleaning', 'Clean the house', 'Low')")
        exec("INSERT INTO task (name, description, priority) VALUES ('gardening', 'Mow the lawn', 'Medium')")
        exec("INSERT INTO task (name, description, priority) VALUES ('shopping', 'Buy the groceries', 'High')")
        exec("INSERT INTO task (name, description, priority) VALUES ('painting', 'Paint the fence', 'Medium')")
        exec("INSERT INTO task (name, description, priority) VALUES ('exercising', 'Walk the dog', 'Medium')")
        exec("INSERT INTO task (name, description, priority) VALUES ('meditating', 'Contemplate the infinite', 'High')")

        // exec("DROP TABLE IF EXISTS TASK")
        // exec("CREATE TABLE IF NOT EXISTS TASK (ID UUID NOT NULL, EMAIL VARCHAR(320) NOT NULL)")
        // exec("INSERT INTO TASK (EMAIL, ID) VALUES ('root1@root.com', '05fb3246-9387-4d04-a27f-fa0107c33883')")
    }
}

fun generateMigrationScript() {
    // Generate a migration script in the specified path
    MigrationUtils.generateMigrationScript(
        TaskTable,
        scriptDirectory = MIGRATIONS_DIRECTORY,
        scriptName = "V2__Add_primary_key_task",
    )
}