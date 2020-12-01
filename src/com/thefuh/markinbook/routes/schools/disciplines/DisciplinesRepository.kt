package com.thefuh.markinbook.routes.schools.disciplines

import com.thefuh.markinbook.data.Discipline
import com.thefuh.markinbook.routes.schools.SchoolEntity
import com.thefuh.markinbook.routes.schools.SchoolsTable
import com.thefuh.markinbook.routes.schools.groups.GroupEntity
import com.thefuh.markinbook.routes.schools.groups.GroupsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DisciplinesRepository {

    fun add(title: String, school: SchoolEntity): DisciplineEntity {
        return DisciplineEntity.new {
            this.title = title
            this.school = school
        }
    }

    fun getAllBySchool(schoolId: Int): SizedIterable<DisciplineEntity> {
        return DisciplineEntity.find { DisciplinesTable.schoolId eq schoolId }
    }

    fun getById(id: Int): DisciplineEntity? {
        return DisciplineEntity.findById(id)
    }
}

object DisciplinesTable : IntIdTable() {
    val title = text("title")
    val schoolId = reference("schoolId", SchoolsTable)
}

class DisciplineEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DisciplineEntity>(DisciplinesTable)

    var title by DisciplinesTable.title
    var school by SchoolEntity referencedOn DisciplinesTable.schoolId
}

fun SizedIterable<DisciplineEntity>.toDisciplines(): List<Discipline> {
    return map { it.toDiscipline() }
}

fun DisciplineEntity.toDiscipline(): Discipline {
    return Discipline(
        id.value,
        title
    )
}