package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ConversationEntity
import com.example.data.local.MessageEntity
import com.example.data.local.MessengerDatabase
import com.example.data.model.ActiveStory
import com.example.data.model.MediaType
import com.example.data.model.PlatformType
import com.example.data.repository.MessengerRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class HubTab(val title: String, val platform: PlatformType?) {
    ALL("All Inboxes", null),
    MESSENGER("Messenger", PlatformType.MESSENGER),
    TWITTER("X DMs", PlatformType.TWITTER_X),
    GMAIL("Gmail", PlatformType.GMAIL),
    YAHOO("Yahoo Mail", PlatformType.YAHOO_MAIL),
    SETTINGS("Settings", null)
}

enum class ConversationFilter {
    ALL,
    UNREAD,
    STARRED
}

data class CallState(
    val isActive: Boolean = false,
    val conversation: ConversationEntity? = null,
    val isVideo: Boolean = false,
    val durationSeconds: Int = 0,
    val isMuted: Boolean = false,
    val isSpeakerOn: Boolean = false
)

class MessengerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MessengerRepository

    init {
        val database = MessengerDatabase.getDatabase(application, viewModelScope)
        repository = MessengerRepository(database.messengerDao())
    }

    private val _selectedTab = MutableStateFlow(HubTab.ALL)
    val selectedTab: StateFlow<HubTab> = _selectedTab.asStateFlow()

    private val _filter = MutableStateFlow(ConversationFilter.ALL)
    val filter: StateFlow<ConversationFilter> = _filter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isWebMode = MutableStateFlow(false)
    val isWebMode: StateFlow<Boolean> = _isWebMode.asStateFlow()

    private val _currentWebUrl = MutableStateFlow("https://www.messenger.com")
    val currentWebUrl: StateFlow<String> = _currentWebUrl.asStateFlow()

    private val _selectedConversationId = MutableStateFlow<String?>(null)
    val selectedConversationId: StateFlow<String?> = _selectedConversationId.asStateFlow()

    private val _currentConversation = MutableStateFlow<ConversationEntity?>(null)
    val currentConversation: StateFlow<ConversationEntity?> = _currentConversation.asStateFlow()

    private val _currentMessages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val currentMessages: StateFlow<List<MessageEntity>> = _currentMessages.asStateFlow()

    private val _activeStories = MutableStateFlow(repository.getActiveStories())
    val activeStories: StateFlow<List<ActiveStory>> = _activeStories.asStateFlow()

    private val _callState = MutableStateFlow(CallState())
    val callState: StateFlow<CallState> = _callState.asStateFlow()

    private var callTimerJob: Job? = null

    // Compose Dialog State
    private val _isComposeOpen = MutableStateFlow(false)
    val isComposeOpen: StateFlow<Boolean> = _isComposeOpen.asStateFlow()

    private val _composePlatform = MutableStateFlow(PlatformType.MESSENGER)
    val composePlatform: StateFlow<PlatformType> = _composePlatform.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    // Conversations combined stream
    val conversations: StateFlow<List<ConversationEntity>> = combine(
        repository.getAllConversations(),
        _selectedTab,
        _filter,
        _searchQuery
    ) { all, tab, filter, query ->
        var list = when (tab.platform) {
            null -> all
            else -> all.filter { it.platform == tab.platform }
        }

        list = when (filter) {
            ConversationFilter.ALL -> list
            ConversationFilter.UNREAD -> list.filter { it.unreadCount > 0 }
            ConversationFilter.STARRED -> list.filter { it.isStarred }
        }

        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                        it.lastMessage.lowercase().contains(q) ||
                        it.subtitleOrHandle.lowercase().contains(q) ||
                        (it.emailSubject?.lowercase()?.contains(q) == true)
            }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: HubTab) {
        _selectedTab.value = tab
        if (tab.platform != null) {
            _currentWebUrl.value = tab.platform.webLoginUrl
        }
    }

    fun setFilter(filter: ConversationFilter) {
        _filter.value = filter
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleWebMode(forceState: Boolean? = null) {
        val next = forceState ?: !_isWebMode.value
        _isWebMode.value = next
    }

    fun setWebUrl(url: String) {
        _currentWebUrl.value = url
    }

    fun openConversation(id: String) {
        _selectedConversationId.value = id
        viewModelScope.launch {
            repository.markAsRead(id)
            val conv = repository.getConversationById(id)
            _currentConversation.value = conv
            repository.getMessages(id).collect { msgs ->
                _currentMessages.value = msgs
            }
        }
    }

    fun closeConversation() {
        _selectedConversationId.value = null
        _currentConversation.value = null
        _currentMessages.value = emptyList()
    }

    fun sendMessage(text: String, mediaType: MediaType = MediaType.NONE) {
        val convId = _selectedConversationId.value ?: return
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.sendMessage(convId, text, mediaType)
        }
    }

    fun toggleStar(conversationId: String) {
        viewModelScope.launch {
            repository.toggleStar(conversationId)
            if (_currentConversation.value?.id == conversationId) {
                _currentConversation.value = repository.getConversationById(conversationId)
            }
        }
    }

    fun deleteConversation(conversationId: String) {
        viewModelScope.launch {
            repository.deleteConversation(conversationId)
            if (_selectedConversationId.value == conversationId) {
                closeConversation()
            }
            showSnackbar("Conversation removed")
        }
    }

    fun addReaction(messageId: String, reaction: String) {
        viewModelScope.launch {
            repository.updateReaction(messageId, reaction)
        }
    }

    // Call Simulation
    fun startCall(conversation: ConversationEntity, isVideo: Boolean) {
        _callState.value = CallState(
            isActive = true,
            conversation = conversation,
            isVideo = isVideo,
            durationSeconds = 0,
            isMuted = false,
            isSpeakerOn = false
        )
        callTimerJob?.cancel()
        callTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _callState.value = _callState.value.copy(
                    durationSeconds = _callState.value.durationSeconds + 1
                )
            }
        }
    }

    fun endCall() {
        callTimerJob?.cancel()
        callTimerJob = null
        _callState.value = CallState(isActive = false)
        showSnackbar("Call ended")
    }

    fun toggleMute() {
        _callState.value = _callState.value.copy(isMuted = !_callState.value.isMuted)
    }

    fun toggleSpeaker() {
        _callState.value = _callState.value.copy(isSpeakerOn = !_callState.value.isSpeakerOn)
    }

    // Compose Handling
    fun openCompose(platform: PlatformType? = null) {
        _composePlatform.value = platform ?: _selectedTab.value.platform ?: PlatformType.MESSENGER
        _isComposeOpen.value = true
    }

    fun closeCompose() {
        _isComposeOpen.value = false
    }

    fun sendComposed(platform: PlatformType, recipient: String, subject: String?, body: String) {
        if (recipient.isBlank() || body.isBlank()) return
        viewModelScope.launch {
            val convId = repository.createNewConversationAndMessage(platform, recipient, subject, body)
            closeCompose()
            showSnackbar("Sent via ${platform.displayName}")
            openConversation(convId)
        }
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}
