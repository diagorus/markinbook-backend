package com.thefuh.markinbook.database.tables.schools.disciplines

import com.thefuh.markinbook.database.tables.schools.SchoolEntity
import org.jetbrains.exposed.sql.SizedIterable

class DisciplinesRepository {

    fun add(title: String, school: SchoolEntity): DisciplineEntity {
        return DisciplineEntity.new {
            this.title = title
            this.school = school
        }
    }

    fun getAll(): SizedIterable<DisciplineEntity> {
        return DisciplineEntity.all()
    }

    fun getById(id: Int): DisciplineEntity? {
        return DisciplineEntity.findById(id)
    }
}