package com.thefuh.markinbook.database.tables.schools

class SchoolsRepository {

    fun add(title: String): SchoolEntity {
        return SchoolEntity.new {
            this.title = title
        }
    }

    fun getById(id: Int): SchoolEntity? {
        return SchoolEntity.findById(id)
    }

    fun getAll(): List<SchoolEntity> {
        return SchoolEntity.all().toList()
    }
}