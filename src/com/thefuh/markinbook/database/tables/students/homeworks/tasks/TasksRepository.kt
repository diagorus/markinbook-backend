package com.thefuh.markinbook.database.tables.students.homeworks.tasks

import com.thefuh.markinbook.database.tables.students.homeworks.HomeworkEntity

class TasksRepository {

    fun add(homework: HomeworkEntity, description: String) : TaskEntity {
        return TaskEntity.new {
            this.homework = homework
            this.description = description
        }
    }
}