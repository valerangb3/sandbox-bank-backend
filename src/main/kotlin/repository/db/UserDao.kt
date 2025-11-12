package com.example.repository.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable("users") {
    val login = varchar("login", 50)
    val email = varchar("email", 100)
    val passwordHash = varchar("password_hash", 500)
    val phone = varchar("phone", 50)
    val lastName = varchar("last_name", 100)
    val firstName = varchar("first_name", 100)
    val patronymic = varchar("patronymic", 100)
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
}