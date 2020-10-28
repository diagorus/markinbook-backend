package com.thefuh.markinbook.database.tables.schools

import org.jetbrains.exposed.sql.SizedIterable

class SchoolsRepository {

    fun add(
        title: String,
        longitude: Double,
        latitude: Double,
    ): SchoolEntity {
        return SchoolEntity.new {
            this.title = title
            this.longitude = longitude
            this.latitude = latitude
        }
    }

    fun getById(id: Int): SchoolEntity? {
        return SchoolEntity.findById(id)
    }

    fun getAll(): SizedIterable<SchoolEntity> {
        return SchoolEntity.all()
    }
}