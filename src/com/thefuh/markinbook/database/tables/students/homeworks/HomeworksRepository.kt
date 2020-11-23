package com.thefuh.markinbook.database.tables.students.homeworks

import com.thefuh.markinbook.database.tables.lessons.LessonEntity

class HomeworksRepository {

    fun add(lesson: LessonEntity): HomeworkEntity {
        return HomeworkEntity.new{
            this.lesson = lesson
        }
    }
}