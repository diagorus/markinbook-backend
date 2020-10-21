package com.thefuh.markinbook.database.tables.schools.disciplines

import com.thefuh.markinbook.database.tables.schools.SchoolEntity

class DisciplinesRepository {

    fun add(title: String, school: SchoolEntity): DisciplineEntity {
        return DisciplineEntity.new {
            this.title = title
            this.school = school
        }
    }

    fun getAll(): List<DisciplineEntity> {
        return DisciplineEntity.all().toList()
    }

    fun getById(id: Int): DisciplineEntity? {
        return DisciplineEntity.findById(id)
    }
}