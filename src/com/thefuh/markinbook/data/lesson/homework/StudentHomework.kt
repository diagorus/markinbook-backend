package com.thefuh.markinbook.data.lesson.homework

import kotlinx.serialization.Serializable

@Serializable
class StudentHomework(val id: Int, val mark: Int?, val tasks: List<Task>)