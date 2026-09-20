package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.PlatformType

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey val id: String,
    val platform: PlatformType,
    val title: String,
    val subtitleOrHandle: String,
    val avatarInitials: String,
    val avatarColor: Long,
    val lastMessage: String,
    val lastMessageTime: String,
    val timestampMs: Long,
    val unreadCount: Int = 0,
    val isStarred: Boolean = false,
    val isOnline: Boolean = false,
    val isArchived: Boolean = false,
    val emailSubject: String? = null,
    val categoryTag: String? = null,
    val hasAttachment: Boolean = false
)
