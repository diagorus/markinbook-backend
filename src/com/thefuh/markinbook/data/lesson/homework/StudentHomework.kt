package com.thefuh.markinbook.data.lesson.homework

import kotlinx.serialization.Serializable

@Serializable
class StudentHomework(val mark: Int?, val tasks: List<Task>)