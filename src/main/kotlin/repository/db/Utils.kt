package com.example.repository.db

import com.example.repository.model.Priority
import com.example.repository.model.Task
import com.example.repository.model.User
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import repository.db.UserDAO

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    suspendTransaction {
        block()
    }

fun userDaoToModel(dao: UserDAO) = User(
    id = dao.id.value.toString(),
    login = dao.login,
    email = dao.email,
    passwordHash = dao.passwordHash,
    phone = dao.phone,
    lastName = dao.lastName,
    firstName = dao.firstName,
    patronymic = dao.patronymic
)

fun taskDaoToModel(dao: TaskDAO) = Task(
    dao.name,
    dao.description,
    Priority.valueOf(dao.priority)
)