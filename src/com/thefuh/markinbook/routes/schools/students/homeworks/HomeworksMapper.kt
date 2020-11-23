package com.thefuh.markinbook.routes.schools.students.homeworks

import com.thefuh.markinbook.data.Lesson.Homework
import com.thefuh.markinbook.database.tables.students.homeworks.HomeworkEntity
import com.thefuh.markinbook.routes.schools.students.homeworks.tasks.toTasks

fun HomeworkEntity.toHomework(): Homework {
    return Homework(
        mark,
        tasks.toTasks()
    )
}