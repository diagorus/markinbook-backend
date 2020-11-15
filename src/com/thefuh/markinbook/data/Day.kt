package com.thefuh.markinbook.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Day {
    @SerialName("monday")
    MONDAY,
    @SerialName("tuesday")
    TUESDAY,
    @SerialName("wednesday")
    WEDNESDAY,
    @SerialName("thursday")
    THURSDAY,
    @SerialName("friday")
    FRIDAY,
    @SerialName("saturday")
    SATURDAY,
    @SerialName("sunday")
    SUNDAY,
}