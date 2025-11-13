package com.example.repository.db

import com.example.repository.model.Priority
import com.example.repository.model.Task
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    suspendTransaction {
        block()
    }

fun taskDaoToModel(dao: TaskDAO) = Task(
    dao.name,
    dao.description,
    Priority.valueOf(dao.priority)
)