package com.thefuh.markinbook.database.tables.schools.disciplines

import com.thefuh.markinbook.database.tables.schools.SchoolEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DisciplineEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DisciplineEntity>(DisciplinesTable)

    var title by DisciplinesTable.title
    var school by SchoolEntity referencedOn DisciplinesTable.schoolId
}