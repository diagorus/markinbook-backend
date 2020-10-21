package com.thefuh.markinbook.data

import com.thefuh.markinbook.data.serializers.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Lesson(
    val group: Group,
    val discipline: Discipline,
    @Serializable(with = DateSerializer::class)
    val start: Long,
    val durationInMinutes: Int,
    val homework: Homework?,
    val mark: Int?,
) {
    @Serializable
    data class Homework(val mark: Int?, val tasks: List<Task>) {

        @Serializable
        data class Task(val done: Boolean, val description: String)
    }
}