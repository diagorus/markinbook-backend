package com.thefuh.markinbook.data.lesson.homework

import com.thefuh.markinbook.data.mark.TeacherMark
import kotlinx.serialization.Serializable

@Serializable
data class TeacherHomework(val id: Int, val marks: List<TeacherMark>, val tasks: List<Task>)