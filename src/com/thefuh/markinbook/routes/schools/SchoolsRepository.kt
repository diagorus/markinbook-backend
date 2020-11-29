package com.thefuh.markinbook.routes.schools

import com.thefuh.markinbook.data.School
import com.thefuh.markinbook.routes.schools.disciplines.DisciplineEntity
import com.thefuh.markinbook.routes.schools.disciplines.DisciplinesTable
import com.thefuh.markinbook.routes.schools.disciplines.toDiscipline
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SizedIterable

class SchoolsRepository {

    fun add(title: String, latitude: Double, longitude: Double): SchoolEntity {
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

object SchoolsTable : IntIdTable() {
    val title = text("title")
    val longitude = double("longitude")
    val latitude = double("latitude")
}

class SchoolEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SchoolEntity>(SchoolsTable)

    var title by SchoolsTable.title
    var latitude by SchoolsTable.latitude
    var longitude by SchoolsTable.longitude

    val disciplines by DisciplineEntity referrersOn DisciplinesTable.schoolId
}

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