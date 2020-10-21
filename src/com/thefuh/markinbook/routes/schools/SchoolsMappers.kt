package com.thefuh.markinbook.routes.schools

import com.thefuh.markinbook.data.School
import com.thefuh.markinbook.database.tables.schools.SchoolEntity
import com.thefuh.markinbook.routes.schools.disciplines.toDiscipline
import org.jetbrains.exposed.sql.SizedIterable


fun SizedIterable<SchoolEntity>.toSchools(): List<School> {
    return map { it.toSchool() }
}

fun SchoolEntity.toSchool(): School {
    return School(
        id.value,
        title,
        disciplines.map { it.toDiscipline() }
    )
}