package com.example.data.repository

import com.example.data.local.ConversationEntity
import com.example.data.local.InitialData
import com.example.data.local.MessageEntity
import com.example.data.local.MessengerDao
import com.example.data.model.ActiveStory
import com.example.data.model.MediaType
import com.example.data.model.PlatformType
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class MessengerRepository(private val dao: MessengerDao) {

    fun getAllConversations(): Flow<List<ConversationEntity>> = dao.getAllConversations()

    fun getConversationsByPlatform(platform: PlatformType): Flow<List<ConversationEntity>> =
        dao.getConversationsByPlatform(platform)

    fun getStarredConversations(): Flow<List<ConversationEntity>> = dao.getStarredConversations()

    fun getUnreadConversations(): Flow<List<ConversationEntity>> = dao.getUnreadConversations()

    fun getMessages(conversationId: String): Flow<List<MessageEntity>> =
        dao.getMessagesForConversation(conversationId)

    suspend fun getConversationById(id: String): ConversationEntity? =
        dao.getConversationById(id)

    fun getActiveStories(): List<ActiveStory> = InitialData.activeStories

    suspend fun sendMessage(
        conversationId: String,
        text: String,
        mediaType: MediaType = MediaType.NONE,
        mediaUrl: String? = null
    ) {
        val now = System.currentTimeMillis()
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val formattedTime = timeFormat.format(Date(now))

        val message = MessageEntity(
            id = "msg_" + UUID.randomUUID().toString(),
            conversationId = conversationId,
            text = text,
            isFromMe = true,
            time = formattedTime,
            timestampMs = now,
            mediaType = mediaType,
            mediaUrl = mediaUrl
        )
        dao.insertMessage(message)

        val conversation = dao.getConversationById(conversationId)
        if (conversation != null) {
            val updated = conversation.copy(
                lastMessage = text,
                lastMessageTime = formattedTime,
                timestampMs = now
            )
            dao.updateConversation(updated)
        }
    }

    suspend fun createNewConversationAndMessage(
        platform: PlatformType,
        recipient: String,
        subject: String?,
        body: String
    ): String {
        val now = System.currentTimeMillis()
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val formattedTime = timeFormat.format(Date(now))
        val convId = "conv_" + UUID.randomUUID().toString().take(8)

        val initials = recipient.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .map { it.first().uppercase() }
            .joinToString("")
            .ifEmpty { recipient.take(2).uppercase() }

        val avatarColor = when (platform) {
            PlatformType.MESSENGER -> 0xFF0084FF
            PlatformType.TWITTER_X -> 0xFF1D9BF0
            PlatformType.GMAIL -> 0xFFEA4335
            PlatformType.YAHOO_MAIL -> 0xFF6001D2
        }

        val conversation = ConversationEntity(
            id = convId,
            platform = platform,
            title = recipient,
            subtitleOrHandle = if (platform == PlatformType.TWITTER_X && !recipient.startsWith("@")) "@$recipient" else recipient,
            avatarInitials = initials,
            avatarColor = avatarColor,
            lastMessage = body,
            lastMessageTime = formattedTime,
            timestampMs = now,
            unreadCount = 0,
            isStarred = false,
            isOnline = true,
            emailSubject = subject,
            categoryTag = if (platform.isEmailService) "Sent" else null
        )
        dao.insertConversation(conversation)

        val message = MessageEntity(
            id = "msg_" + UUID.randomUUID().toString(),
            conversationId = convId,
            text = body,
            isFromMe = true,
            time = formattedTime,
            timestampMs = now,
            mediaType = MediaType.NONE
        )
        dao.insertMessage(message)
        return convId
    }

    suspend fun toggleStar(id: String) {
        dao.toggleStar(id)
    }

    suspend fun markAsRead(id: String) {
        dao.markAsRead(id)
    }

    suspend fun deleteConversation(id: String) {
        dao.deleteConversation(id)
    }

    suspend fun archiveConversation(id: String) {
        dao.archiveConversation(id)
    }

    suspend fun updateReaction(messageId: String, reaction: String?) {
        dao.updateReaction(messageId, reaction)
    }
}
