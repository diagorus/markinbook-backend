package com.thefuh.markinbook.utils

import com.thefuh.markinbook.routes.schools.disciplines.DisciplinesTable
import com.thefuh.markinbook.routes.schools.groups.GroupsTable
import com.thefuh.markinbook.routes.schools.students.StudentsTable
import com.thefuh.markinbook.routes.schools.students.lessons.LessonsTable
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.HomeworksTable
import com.thefuh.markinbook.routes.schools.students.lessons.homeworks.tasks.TasksTable
import com.thefuh.markinbook.routes.schools.students.lessons.marks.MarksTable
import com.thefuh.markinbook.routes.schools.teachers.TeachersTable
import com.thefuh.markinbook.routes.users.UsersTable
import com.thefuh.markinbook.routes.users.tokens.PushTokensTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        Database.connect(hikari())

        transaction {
            exec(
                "DO \$\$ BEGIN\n" +
                    "    CREATE TYPE Role AS ENUM ('TEACHER', 'STUDENT', 'PARENT');\n" +
                    "EXCEPTION\n" +
                    "    WHEN duplicate_object THEN null;\n" +
                    "END \$\$;"
            )

            SchemaUtils.create(PushTokensTable)
            SchemaUtils.create(UsersTable)
            SchemaUtils.create(StudentsTable)
            SchemaUtils.create(TeachersTable)

            SchemaUtils.create(TasksTable)
            SchemaUtils.create(HomeworksTable)
            SchemaUtils.create(GroupsTable)
            SchemaUtils.create(DisciplinesTable)
            SchemaUtils.create(LessonsTable)
            SchemaUtils.create(MarksTable)
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = System.getenv("JDBC_DRIVER")
            jdbcUrl = System.getenv("JDBC_DATABASE_URL")
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }
        val user = System.getenv("DB_USER")
        if (user != null) {
            config.username = user
        }
        val password = System.getenv("DB_PASSWORD")
        if (password != null) {
            config.password = password
        }
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: Transaction.() -> T): T =
        withContext(Dispatchers.IO) {
            transaction { this.block() }
        }
}