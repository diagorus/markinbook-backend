package com.thefuh.markinbook.data.lesson.homework

import com.thefuh.markinbook.data.mark.StudentMark
import kotlinx.serialization.Serializable

@Serializable
class StudentHomework(val id: Int, val mark: StudentMark?, val tasks: List<Task>)