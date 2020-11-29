package com.thefuh.markinbook.data.mark

import kotlinx.serialization.Serializable

@Serializable
data class TeacherMark(val studentId: Int, val value: Int)