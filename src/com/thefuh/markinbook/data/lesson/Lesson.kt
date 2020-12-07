package com.thefuh.markinbook.data.lesson

import com.thefuh.markinbook.data.Discipline
import com.thefuh.markinbook.data.Group
import com.thefuh.markinbook.data.lesson.homework.StudentHomework

interface Lesson {
    val id: Int
    val group: Group
    val discipline: Discipline
    val start: Long
    val durationInMinutes: Int
}