package com.example.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.data.model.MediaType

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ConversationEntity::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["conversationId"])]
)
data class MessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val text: String,
    val isFromMe: Boolean,
    val time: String,
    val timestampMs: Long,
    val mediaType: MediaType = MediaType.NONE,
    val mediaUrl: String? = null,
    val reaction: String? = null,
    val attachmentName: String? = null,
    val attachmentSize: String? = null
)
