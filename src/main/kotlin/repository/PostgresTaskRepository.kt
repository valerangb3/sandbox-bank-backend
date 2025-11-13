package com.example.repository

import com.example.repository.db.TaskDAO
import com.example.repository.db.TaskTable
import com.example.repository.db.taskDaoToModel
import com.example.repository.db.suspendTransaction
import com.example.repository.model.Priority
import com.example.repository.model.Task
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere

class PostgresTaskRepository : TaskRepository {
    override suspend fun allTasks(): List<Task> = suspendTransaction {
        TaskDAO.Companion.all().map(::taskDaoToModel)
    }

    override suspend fun tasksByPriority(priority: Priority): List<Task> = suspendTransaction {
        TaskDAO.Companion
            .find { (TaskTable.priority eq priority.toString()) }
            .map(::taskDaoToModel)
    }

    override suspend fun taskByName(name: String): Task? = suspendTransaction {
        TaskDAO.Companion
            .find { (TaskTable.name eq name) }
            .limit(1)
            .map(::taskDaoToModel)
            .firstOrNull()
    }

    override suspend fun addTask(task: Task): Unit = suspendTransaction {
        TaskDAO.Companion.new {
            name = task.name
            description = task.description
            priority = task.priority.toString()
        }
    }

    override suspend fun removeTask(name: String): Boolean = suspendTransaction {
        val rowsDeleted = TaskTable.deleteWhere {
            TaskTable.name eq name
        }
        rowsDeleted == 1
    }
}