package com.thefuh.markinbook.routes.schools.disciplines

import com.thefuh.markinbook.data.Discipline
import com.thefuh.markinbook.database.tables.schools.disciplines.DisciplineEntity
import org.jetbrains.exposed.sql.SizedIterable

fun SizedIterable<DisciplineEntity>.toDisciplines(): List<Discipline> {
    return map { it.toDiscipline() }
}

fun DisciplineEntity.toDiscipline(): Discipline {
    return Discipline(
        id.value,
        title
    )
}