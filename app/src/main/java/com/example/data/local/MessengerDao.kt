package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.PlatformType
import kotlinx.coroutines.flow.Flow

@Dao
interface MessengerDao {
    @Query("SELECT * FROM conversations WHERE isArchived = 0 ORDER BY timestampMs DESC")
    fun getAllConversations(): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE platform = :platform AND isArchived = 0 ORDER BY timestampMs DESC")
    fun getConversationsByPlatform(platform: PlatformType): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE isStarred = 1 AND isArchived = 0 ORDER BY timestampMs DESC")
    fun getStarredConversations(): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE unreadCount > 0 AND isArchived = 0 ORDER BY timestampMs DESC")
    fun getUnreadConversations(): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE id = :id")
    suspend fun getConversationById(id: String): ConversationEntity?

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestampMs ASC")
    fun getMessagesForConversation(conversationId: String): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(conversations: List<ConversationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Update
    suspend fun updateConversation(conversation: ConversationEntity)

    @Query("UPDATE conversations SET isStarred = NOT isStarred WHERE id = :id")
    suspend fun toggleStar(id: String)

    @Query("UPDATE conversations SET unreadCount = 0 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query("UPDATE conversations SET isArchived = 1 WHERE id = :id")
    suspend fun archiveConversation(id: String)

    @Query("DELETE FROM conversations WHERE id = :id")
    suspend fun deleteConversation(id: String)

    @Query("SELECT COUNT(*) FROM conversations")
    suspend fun getConversationCount(): Int

    @Query("UPDATE messages SET reaction = :reaction WHERE id = :messageId")
    suspend fun updateReaction(messageId: String, reaction: String?)
}
