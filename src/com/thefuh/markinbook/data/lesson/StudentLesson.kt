package com.thefuh.markinbook.data.lesson

import com.thefuh.markinbook.data.Discipline
import com.thefuh.markinbook.data.Group
import com.thefuh.markinbook.data.lesson.homework.StudentHomework
import com.thefuh.markinbook.data.mark.StudentMark
import kotlinx.serialization.Serializable

@Serializable
class StudentLesson(
    override val id: Int,
    override val group: Group,
    override val discipline: Discipline,
    override val start: Long,
    override val durationInMinutes: Int,
    val homework: StudentHomework?,
    val mark: StudentMark?,
) : Lesson