package com.example.data.local

import com.example.data.model.ActiveStory
import com.example.data.model.MediaType
import com.example.data.model.PlatformType

object InitialData {
    val activeStories = listOf(
        ActiveStory("story_1", "Sarah J.", "SJ", 0xFF0084FF, true, "Coffee time ☕"),
        ActiveStory("story_2", "Alex R.", "AR", 0xFF6001D2, true, "Weekend vibes!"),
        ActiveStory("story_3", "Mark Z.", "MZ", 0xFF1D9BF0, true, "Building AI..."),
        ActiveStory("story_4", "Elena K.", "EK", 0xFFEA4335, false, "At the beach 🏖️"),
        ActiveStory("story_5", "David M.", "DM", 0xFF10B981, true, "Hiking summit 🏔️"),
        ActiveStory("story_6", "Sophia L.", "SL", 0xFFF59E0B, false, "Coding all night")
    )

    fun getInitialConversations(): List<ConversationEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            // Facebook Messenger
            ConversationEntity(
                id = "fb_1",
                platform = PlatformType.MESSENGER,
                title = "Sarah Jenkins",
                subtitleOrHandle = "Active now",
                avatarInitials = "SJ",
                avatarColor = 0xFF0084FF,
                lastMessage = "Are you free for a quick call this afternoon?",
                lastMessageTime = "10:15 AM",
                timestampMs = now - 1000 * 60 * 15,
                unreadCount = 2,
                isStarred = true,
                isOnline = true,
                categoryTag = null
            ),
            ConversationEntity(
                id = "fb_2",
                platform = PlatformType.MESSENGER,
                title = "Alex Rivera",
                subtitleOrHandle = "Active 20m ago",
                avatarInitials = "AR",
                avatarColor = 0xFF3B82F6,
                lastMessage = "Sent you the concert photos from yesterday! 🎸",
                lastMessageTime = "Yesterday",
                timestampMs = now - 1000 * 60 * 60 * 18,
                unreadCount = 0,
                isStarred = false,
                isOnline = false,
                hasAttachment = true
            ),
            ConversationEntity(
                id = "fb_3",
                platform = PlatformType.MESSENGER,
                title = "Tech Innovators Group",
                subtitleOrHandle = "Mark, Liam, David and 12 others",
                avatarInitials = "TI",
                avatarColor = 0xFF8B5CF6,
                lastMessage = "Liam: Check out the new Compose Multiplatform release.",
                lastMessageTime = "Sep 18",
                timestampMs = now - 1000 * 60 * 60 * 40,
                unreadCount = 5,
                isStarred = false,
                isOnline = true
            ),

            // Twitter / X DMs
            ConversationEntity(
                id = "tw_1",
                platform = PlatformType.TWITTER_X,
                title = "Elon Musk",
                subtitleOrHandle = "@elonmusk",
                avatarInitials = "EM",
                avatarColor = 0xFF1D9BF0,
                lastMessage = "Starship launch countdown looking good for tomorrow morning 🚀",
                lastMessageTime = "11:42 AM",
                timestampMs = now - 1000 * 60 * 45,
                unreadCount = 1,
                isStarred = true,
                isOnline = true
            ),
            ConversationEntity(
                id = "tw_2",
                platform = PlatformType.TWITTER_X,
                title = "TechCrunch",
                subtitleOrHandle = "@TechCrunch",
                avatarInitials = "TC",
                avatarColor = 0xFF10B981,
                lastMessage = "Exclusive report: Next generation mobile AI assistants unveiled.",
                lastMessageTime = "8:30 AM",
                timestampMs = now - 1000 * 60 * 60 * 3,
                unreadCount = 0,
                isStarred = false,
                isOnline = true
            ),
            ConversationEntity(
                id = "tw_3",
                platform = PlatformType.TWITTER_X,
                title = "Kotlin Developers",
                subtitleOrHandle = "@kotlin",
                avatarInitials = "KD",
                avatarColor = 0xFFF97316,
                lastMessage = "Kotlin 2.2 is here with faster build speeds and new language features!",
                lastMessageTime = "Sep 16",
                timestampMs = now - 1000 * 60 * 60 * 70,
                unreadCount = 0,
                isStarred = false,
                isOnline = false
            ),

            // Gmail
            ConversationEntity(
                id = "gm_1",
                platform = PlatformType.GMAIL,
                title = "Google Cloud Platform",
                subtitleOrHandle = "cloud-notifications@google.com",
                avatarInitials = "GC",
                avatarColor = 0xFFEA4335,
                lastMessage = "Your monthly Cloud Architecture report is ready. Usage optimized by 18%...",
                lastMessageTime = "9:05 AM",
                timestampMs = now - 1000 * 60 * 90,
                unreadCount = 1,
                isStarred = true,
                isOnline = false,
                emailSubject = "Monthly Cloud Billing & Architecture Summary",
                categoryTag = "Primary",
                hasAttachment = true
            ),
            ConversationEntity(
                id = "gm_2",
                platform = PlatformType.GMAIL,
                title = "GitHub Security",
                subtitleOrHandle = "support@github.com",
                avatarInitials = "GH",
                avatarColor = 0xFF24292E,
                lastMessage = "All security checks passed for your latest pull request on main branch.",
                lastMessageTime = "Yesterday",
                timestampMs = now - 1000 * 60 * 60 * 22,
                unreadCount = 0,
                isStarred = false,
                isOnline = false,
                emailSubject = "[GitHub] Security advisory clean and dependencies updated",
                categoryTag = "Updates"
            ),
            ConversationEntity(
                id = "gm_3",
                platform = PlatformType.GMAIL,
                title = "Elena Rostova",
                subtitleOrHandle = "elena.design@agency.io",
                avatarInitials = "ER",
                avatarColor = 0xFFEC4899,
                lastMessage = "Hi! Attached are the final approved UI Figma components and export assets.",
                lastMessageTime = "Sep 17",
                timestampMs = now - 1000 * 60 * 60 * 50,
                unreadCount = 0,
                isStarred = true,
                isOnline = false,
                emailSubject = "Final Approved UI Design System & Assets",
                categoryTag = "Primary",
                hasAttachment = true
            ),

            // Yahoo Mail
            ConversationEntity(
                id = "yh_1",
                platform = PlatformType.YAHOO_MAIL,
                title = "Yahoo Finance Daily",
                subtitleOrHandle = "finance-alert@yahoo.com",
                avatarInitials = "YF",
                avatarColor = 0xFF6001D2,
                lastMessage = "Market Movers: Technology indices surge 3.8% amid breakthrough AI announcements.",
                lastMessageTime = "7:45 AM",
                timestampMs = now - 1000 * 60 * 60 * 4,
                unreadCount = 3,
                isStarred = true,
                isOnline = false,
                emailSubject = "Market Watch: Tech Surge Leads Global Gains",
                categoryTag = "Finance"
            ),
            ConversationEntity(
                id = "yh_2",
                platform = PlatformType.YAHOO_MAIL,
                title = "Amazon Orders",
                subtitleOrHandle = "shipment-tracking@amazon.com",
                avatarInitials = "AZ",
                avatarColor = 0xFFFF9900,
                lastMessage = "Your package #402-99120 has shipped and will arrive tomorrow by 8 PM.",
                lastMessageTime = "Sep 18",
                timestampMs = now - 1000 * 60 * 60 * 36,
                unreadCount = 0,
                isStarred = false,
                isOnline = false,
                emailSubject = "Your Amazon Package is on the way! 📦",
                categoryTag = "Shopping",
                hasAttachment = true
            ),
            ConversationEntity(
                id = "yh_3",
                platform = PlatformType.YAHOO_MAIL,
                title = "Travelocity Deals",
                subtitleOrHandle = "offers@travelocity.com",
                avatarInitials = "TV",
                avatarColor = 0xFF00D3B0,
                lastMessage = "Weekend Getaway Alert: 40% Off flights and boutique hotels in Asia & Europe.",
                lastMessageTime = "Sep 15",
                timestampMs = now - 1000 * 60 * 60 * 95,
                unreadCount = 0,
                isStarred = false,
                isOnline = false,
                emailSubject = "Exclusive 40% Vacation Flash Sale",
                categoryTag = "Deals"
            )
        )
    }

    fun getInitialMessages(): List<MessageEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            // Facebook Sarah Jenkins
            MessageEntity("msg_fb_1", "fb_1", "Hey! How is the project coming along?", false, "10:10 AM", now - 1000 * 60 * 20),
            MessageEntity("msg_fb_2", "fb_1", "Going really well! Just integrated the all-in-one messaging views.", true, "10:12 AM", now - 1000 * 60 * 18, reaction = "👍"),
            MessageEntity("msg_fb_3", "fb_1", "That sounds super awesome! 🤩", false, "10:14 AM", now - 1000 * 60 * 16),
            MessageEntity("msg_fb_4", "fb_1", "Are you free for a quick call this afternoon?", false, "10:15 AM", now - 1000 * 60 * 15),

            // Facebook Alex Rivera
            MessageEntity("msg_fb_5", "fb_2", "Yo! The festival was insane last night 🔥", false, "Yesterday 8:00 PM", now - 1000 * 60 * 60 * 19),
            MessageEntity("msg_fb_6", "fb_2", "Sent you the concert photos from yesterday! 🎸", false, "Yesterday 8:15 PM", now - 1000 * 60 * 60 * 18, mediaType = MediaType.PHOTO, mediaUrl = "photo_concert"),

            // Twitter Elon Musk
            MessageEntity("msg_tw_1", "tw_1", "Hello! The new orbital flight test is on track.", false, "11:30 AM", now - 1000 * 60 * 60),
            MessageEntity("msg_tw_2", "tw_1", "Awesome! What's the main focus of this booster flight?", true, "11:35 AM", now - 1000 * 60 * 55),
            MessageEntity("msg_tw_3", "tw_1", "Starship launch countdown looking good for tomorrow morning 🚀", false, "11:42 AM", now - 1000 * 60 * 45, reaction = "🔥"),

            // Twitter TechCrunch
            MessageEntity("msg_tw_4", "tw_2", "Exclusive report: Next generation mobile AI assistants unveiled.", false, "8:30 AM", now - 1000 * 60 * 60 * 3, mediaType = MediaType.TWEET),

            // Gmail Google Cloud
            MessageEntity(
                "msg_gm_1",
                "gm_1",
                "Hello Developer,\n\nWe have generated your monthly infrastructure summary for Project All-Messenger-Hub.\n\nSummary highlights:\n- Serverless containers: 99.99% uptime\n- Database latency: < 5ms\n- Cloud CDN egress optimization: Saved $140 this billing period.\n\nPlease find the attached comprehensive billing PDF and telemetry architecture charts below.\n\nBest regards,\nThe Google Cloud Platform Team",
                false,
                "9:05 AM",
                now - 1000 * 60 * 90,
                mediaType = MediaType.ATTACHMENT,
                attachmentName = "GCP_Monthly_Report_Sep2026.pdf",
                attachmentSize = "2.4 MB"
            ),

            // Gmail Elena Rostova
            MessageEntity(
                "msg_gm_2",
                "gm_3",
                "Hi there,\n\nHere are the finalized high-fidelity assets for the unified messenger project:\n\n- Adaptive M3 icons (FB Messenger, Twitter X, Gmail, Yahoo)\n- Custom gradient palettes\n- Clean dark mode elevation specs\n\nLet me know if you need any adjustments to the typography or padding values.\n\nWarm regards,\nElena Rostova\nLead UX Designer",
                false,
                "Sep 17",
                now - 1000 * 60 * 60 * 50,
                mediaType = MediaType.ATTACHMENT,
                attachmentName = "Unified_Messenger_UI_Kits.zip",
                attachmentSize = "14.8 MB"
            ),

            // Yahoo Finance
            MessageEntity(
                "msg_yh_1",
                "yh_1",
                "DAILY MARKET WRAP:\n\nGlobal equities experienced strong momentum today led by technological innovation and enterprise software earnings.\n\n• S&P 500: +1.9%\n• NASDAQ: +2.8%\n• Semiconductor Sector: +4.1%\n\nKey highlights include major cloud providers announcing next-gen multi-device communication suites that combine email and messaging into unified workspaces.\n\nRead the full analysis in Yahoo Finance Mobile.",
                false,
                "7:45 AM",
                now - 1000 * 60 * 60 * 4
            ),

            // Yahoo Amazon
            MessageEntity(
                "msg_yh_2",
                "yh_2",
                "Hello,\n\nGood news! Your order #402-99120 containing 'Noise Cancelling Studio Headphones' has been dispatched from our regional fulfillment center.\n\nEstimated Delivery:\nTomorrow, by 8:00 PM\nCarrier: Amazon Priority Courier\nTracking Number: TBA9901827418\n\nThank you for shopping with Amazon!",
                false,
                "Sep 18",
                now - 1000 * 60 * 60 * 36,
                mediaType = MediaType.ATTACHMENT,
                attachmentName = "Amazon_Invoice_402-99120.pdf",
                attachmentSize = "410 KB"
            )
        )
    }
}
