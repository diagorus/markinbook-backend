package com.thefuh.markinbook.database.tables.schools.students.homeworks

import com.thefuh.markinbook.database.tables.schools.students.lessons.LessonEntity

class HomeworksRepository {

    fun add(lesson: LessonEntity): HomeworkEntity {
        return HomeworkEntity.new{
            this.lesson = lesson
        }
    }
}