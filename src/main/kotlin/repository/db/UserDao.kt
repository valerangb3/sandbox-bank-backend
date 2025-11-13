package repository.db

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object UserTable : IntIdTable("users") {
    val login = varchar("login", 50)
    val email = varchar("email", 100)
    val passwordHash = varchar("password_hash", 500)
    val phone = varchar("phone", 50)
    val lastName = varchar("last_name", 100)
    val firstName = varchar("first_name", 100)
    val patronymic = varchar("patronymic", 100)
    val accessToken = varchar("access_token", 300)
    val refreshToken = varchar("refresh_token", 300)
}

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(UserTable)

    var login by UserTable.login
    var email by UserTable.email
    var passwordHash by UserTable.passwordHash
    var phone by UserTable.phone
    var lastName by UserTable.lastName
    var firstName by UserTable.firstName
    var patronymic by UserTable.patronymic
    var accessToken by UserTable.accessToken
    var refreshToken by UserTable.refreshToken
}