package com.thefuh.markinbook.routes.users.tokens

import com.thefuh.markinbook.routes.users.UserEntity
import com.thefuh.markinbook.routes.users.UsersTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.select

class PushTokensRepository {

    fun add(user: UserEntity, token: String): PushTokenEntity {
        return PushTokenEntity.new {
            this.user = user
            this.token = token
        }
    }

    fun getByUserId(userId: Int): SizedIterable<PushTokenEntity> {
        return PushTokenEntity.find { PushTokensTable.userId eq userId }
    }

    fun getTokensByUserIds(userIds: List<Int>): List<String> {
        return PushTokensTable.slice(PushTokensTable.token).select { PushTokensTable.id inList userIds }.map { it[PushTokensTable.token] }
    }
}

object PushTokensTable : IntIdTable() {
    val userId = reference("userId", UsersTable)
    val token = text("token")
}

class PushTokenEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PushTokenEntity>(PushTokensTable)

    var user by UserEntity referencedOn PushTokensTable.userId
    var token by PushTokensTable.token
}