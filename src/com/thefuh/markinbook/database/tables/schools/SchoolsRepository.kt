package com.thefuh.markinbook.database.tables.schools

import org.jetbrains.exposed.sql.SizedIterable

class SchoolsRepository {

    fun add(
        title: String,
        latitude: Double,
        longitude: Double,
    ): SchoolEntity {
        return SchoolEntity.new {
            this.title = title
            this.latitude = latitude
            this.longitude = longitude
        }
    }

    fun getById(id: Int): SchoolEntity? {
        return SchoolEntity.findById(id)
    }

    fun getAll(): SizedIterable<SchoolEntity> {
        return SchoolEntity.all()
    }
}