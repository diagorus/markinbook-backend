package com.thefuh.markinbook.data.lesson

import com.thefuh.markinbook.data.Discipline
import com.thefuh.markinbook.data.Group
import com.thefuh.markinbook.data.lesson.homework.TeacherHomework
import com.thefuh.markinbook.data.mark.TeacherMark
import kotlinx.serialization.Serializable

@Serializable
class TeacherLesson(
    override val group: Group,
    override val discipline: Discipline,
    override val start: Long,
    override val durationInMinutes: Int,
    val homework: TeacherHomework?,
    val marks: List<TeacherMark>,
) : Lesson