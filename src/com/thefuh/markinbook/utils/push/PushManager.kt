package com.thefuh.markinbook.utils.push

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.thefuh.markinbook.DatabaseFactory.dbQuery
import com.thefuh.markinbook.routes.users.tokens.PushTokensRepository

import com.google.firebase.messaging.MulticastMessage
import com.thefuh.markinbook.routes.schools.groups.GroupsRepository
import org.slf4j.Logger

class PushManager(
    private val pushTokensRepository: PushTokensRepository,
    private val groupsRepository: GroupsRepository,
    private val log: Logger
) {

    companion object {
        private const val KEY_TYPE = "type"
        private const val KEY_HOMEWORK_ID = "homeworkId"
        private const val KEY_LESSON_ID = "lessonId"
        private const val KEY_DISCIPLINE_TITLE = "disciplineTitle"
    }

    suspend fun pushHomeworkMark(userId: Int, homeworkId: Int, disciplineTitle: String) {
        val token = getUserToken(userId)
        val message = Message.builder()
            .putData(KEY_TYPE, PushType.HOMEWORK_MARK.title)
            .putData(KEY_HOMEWORK_ID, homeworkId.toString())
            .putData(KEY_DISCIPLINE_TITLE, disciplineTitle)
            .setToken(token)
            .build()
        val response = FirebaseMessaging.getInstance().send(message)
        log.info("Successfully sent message: $response")
    }

    suspend fun pushLessonMark(userId: Int, lessonId: Int, disciplineTitle: String) {
        val token = getUserToken(userId)
        val message = Message.builder()
            .putData(KEY_TYPE, PushType.LESSON_MARK.title)
            .putData(KEY_LESSON_ID, lessonId.toString())
            .putData(KEY_DISCIPLINE_TITLE, disciplineTitle)
            .setToken(token)
            .build()
        val response = FirebaseMessaging.getInstance().send(message)
        log.info("Successfully sent message: $response")
    }

    suspend fun pushHomeworkAdded(groupId: Int, homeworkId: Int, disciplineTitle: String) {
        val tokens = getGroupUserTokens(groupId)

        val message = MulticastMessage.builder()
            .putData(KEY_TYPE, PushType.HOMEWORK_ADDED.title)
            .putData(KEY_HOMEWORK_ID, homeworkId.toString())
            .putData(KEY_DISCIPLINE_TITLE, disciplineTitle)
            .addAllTokens(tokens)
            .build()
        val response = FirebaseMessaging.getInstance().sendMulticast(message)
        log.info("${response.successCount} messages were sent successfully")
    }

    private suspend fun getUserToken(userId: Int) = dbQuery { pushTokensRepository.getByUserId(userId).single().token }

    private suspend fun getGroupUserTokens(groupId: Int) = dbQuery {
        val userIds = groupsRepository.getById(groupId)!!.students.map { it.id.value }
        pushTokensRepository.getTokensByUserIds(userIds)
    }

    enum class PushType(val title: String) {
        HOMEWORK_MARK("homeworkMark"),
        LESSON_MARK("lessonMark"),
        HOMEWORK_ADDED("homeworkAdded")
    }
}