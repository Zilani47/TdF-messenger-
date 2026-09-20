package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.GmailRed
import com.example.ui.theme.MessengerBlue
import com.example.ui.theme.TwitterBlue
import com.example.ui.theme.YahooPurple

enum class PlatformType(
    val id: String,
    val displayName: String,
    val shortName: String,
    val brandColor: Color,
    val webLoginUrl: String,
    val iconEmoji: String,
    val allowsCalls: Boolean,
    val isEmailService: Boolean
) {
    MESSENGER(
        id = "messenger",
        displayName = "Facebook Messenger",
        shortName = "Messenger",
        brandColor = MessengerBlue,
        webLoginUrl = "https://www.messenger.com",
        iconEmoji = "💬",
        allowsCalls = true,
        isEmailService = false
    ),
    TWITTER_X(
        id = "twitter_x",
        displayName = "Twitter / X DMs",
        shortName = "X DMs",
        brandColor = TwitterBlue,
        webLoginUrl = "https://x.com/messages",
        iconEmoji = "𝕏",
        allowsCalls = false,
        isEmailService = false
    ),
    GMAIL(
        id = "gmail",
        displayName = "Gmail Inbox",
        shortName = "Gmail",
        brandColor = GmailRed,
        webLoginUrl = "https://mail.google.com",
        iconEmoji = "✉️",
        allowsCalls = false,
        isEmailService = true
    ),
    YAHOO_MAIL(
        id = "yahoo_mail",
        displayName = "Yahoo Mailbox",
        shortName = "Yahoo Mail",
        brandColor = YahooPurple,
        webLoginUrl = "https://mail.yahoo.com",
        iconEmoji = "🟣",
        allowsCalls = false,
        isEmailService = true
    )
}

enum class MediaType {
    NONE,
    PHOTO,
    VOICE,
    TWEET,
    ATTACHMENT
}

data class ActiveStory(
    val id: String,
    val name: String,
    val avatarInitials: String,
    val avatarColor: Long,
    val isOnline: Boolean,
    val storyText: String? = null
)
