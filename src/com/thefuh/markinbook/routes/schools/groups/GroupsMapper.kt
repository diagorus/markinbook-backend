package com.thefuh.markinbook.routes.schools.groups

import com.thefuh.markinbook.data.Group
import com.thefuh.markinbook.database.tables.schools.students.groups.GroupEntity
import org.jetbrains.exposed.sql.SizedIterable

fun GroupEntity.toGroup(): Group {
    return Group(
        id.value,
        title,
    )
}

fun SizedIterable<GroupEntity>.toGroups(): List<Group> {
    return map { it.toGroup() }
}