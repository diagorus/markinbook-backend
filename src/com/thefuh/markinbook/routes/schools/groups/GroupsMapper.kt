package com.thefuh.markinbook.routes.schools.groups

import com.thefuh.markinbook.data.Group
import com.thefuh.markinbook.database.tables.schools.students.groups.GroupEntity

fun GroupEntity.toGroup(): Group {
    return Group(
        id.value,
        title,
    )
}