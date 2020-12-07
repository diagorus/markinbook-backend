package com.thefuh.markinbook.data

import com.thefuh.markinbook.data.roles.Student
import kotlinx.serialization.Serializable

@Serializable
class Group(val id: Int, val title: String, val students: List<Student>)