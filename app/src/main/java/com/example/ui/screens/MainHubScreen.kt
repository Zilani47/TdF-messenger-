package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlatformType
import com.example.ui.components.ActiveCallScreen
import com.example.ui.components.ComposeDialog
import com.example.ui.components.ConversationDetailScreen
import com.example.ui.components.ConversationItem
import com.example.ui.components.SettingsScreen
import com.example.ui.components.StoryRow
import com.example.ui.components.WebPortalScreen
import com.example.ui.theme.MessengerBlue
import com.example.ui.viewmodel.ConversationFilter
import com.example.ui.viewmodel.HubTab
import com.example.ui.viewmodel.MessengerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHubScreen(
    viewModel: MessengerViewModel,
    modifier: Modifier = Modifier
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isWebMode by viewModel.isWebMode.collectAsState()
    val currentWebUrl by viewModel.currentWebUrl.collectAsState()
    val conversations by viewModel.conversations.collectAsState()
    val selectedConvId by viewModel.selectedConversationId.collectAsState()
    val currentConv by viewModel.currentConversation.collectAsState()
    val currentMessages by viewModel.currentMessages.collectAsState()
    val activeStories by viewModel.activeStories.collectAsState()
    val callState by viewModel.callState.collectAsState()
    val isComposeOpen by viewModel.isComposeOpen.collectAsState()
    val composePlatform by viewModel.composePlatform.collectAsState()
    val snackbarMsg by viewModel.snackbarMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var isSearchExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    // Call Overlay
    if (callState.isActive) {
        ActiveCallScreen(
            callState = callState,
            onEndCall = { viewModel.endCall() },
            onToggleMute = { viewModel.toggleMute() },
            onToggleSpeaker = { viewModel.toggleSpeaker() }
        )
        return
    }

    // Detail Conversation Screen Overlay
    if (selectedConvId != null && currentConv != null) {
        ConversationDetailScreen(
            conversation = currentConv!!,
            messages = currentMessages,
            onBack = { viewModel.closeConversation() },
            onSendMessage = { text -> viewModel.sendMessage(text) },
            onSendPhoto = { viewModel.sendMessage("Attached a photo 📷", com.example.data.model.MediaType.PHOTO) },
            onToggleStar = { viewModel.toggleStar(currentConv!!.id) },
            onDelete = { viewModel.deleteConversation(currentConv!!.id) },
            onStartCall = { isVideo -> viewModel.startCall(currentConv!!, isVideo) },
            onAddReaction = { msgId, reaction -> viewModel.addReaction(msgId, reaction) }
        )
        return
    }

    // Compose Dialog
    if (isComposeOpen) {
        ComposeDialog(
            initialPlatform = composePlatform,
            onDismiss = { viewModel.closeCompose() },
            onSend = { platform, recipient, subject, body ->
                viewModel.sendComposed(platform, recipient, subject, body)
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = selectedTab.platform?.brandColor ?: MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = selectedTab.platform?.iconEmoji ?: "💬",
                                    fontSize = 16.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = selectedTab.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isWebMode) "Official Web Portal" else "Unified Native Hub",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    // Search Button
                    IconButton(
                        onClick = { isSearchExpanded = !isSearchExpanded },
                        modifier = Modifier.testTag("search_toggle_button")
                    ) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }

                    // Native vs Live Web Mode Switcher
                    IconButton(
                        onClick = { viewModel.toggleWebMode() },
                        modifier = Modifier.testTag("mode_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isWebMode) Icons.Default.Smartphone else Icons.Default.Language,
                            contentDescription = if (isWebMode) "Switch to Native Mode" else "Switch to Web Portal",
                            tint = if (isWebMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                HubTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectTab(tab) },
                        icon = {
                            Text(
                                text = when (tab) {
                                    HubTab.ALL -> "🌐"
                                    HubTab.MESSENGER -> "💬"
                                    HubTab.TWITTER -> "𝕏"
                                    HubTab.GMAIL -> "✉️"
                                    HubTab.YAHOO -> "🟣"
                                    HubTab.SETTINGS -> "⚙️"
                                },
                                fontSize = 18.sp
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 10.sp,
                                maxLines = 1,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = tab.platform?.brandColor ?: MaterialTheme.colorScheme.primary,
                            indicatorColor = (tab.platform?.brandColor ?: MaterialTheme.colorScheme.primary).copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                    )
                }
            }
        },
        floatingActionButton = {
            if (selectedTab != HubTab.SETTINGS && !isWebMode) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.openCompose() },
                    icon = { Icon(imageVector = Icons.Default.Edit, contentDescription = "Compose") },
                    text = {
                        Text(
                            text = when (selectedTab.platform) {
                                PlatformType.GMAIL, PlatformType.YAHOO_MAIL -> "Compose Email"
                                else -> "New Message"
                            },
                            fontWeight = FontWeight.Bold
                        )
                    },
                    containerColor = selectedTab.platform?.brandColor ?: MessengerBlue,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("hub_compose_fab")
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search field
            AnimatedVisibility(visible = isSearchExpanded) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Search messages, senders, or emails...") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("search_text_input"),
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }

            // Web Portal Mode
            if (isWebMode) {
                WebPortalScreen(
                    initialUrl = currentWebUrl,
                    onCloseWebMode = { viewModel.toggleWebMode(false) },
                    modifier = Modifier.fillMaxSize()
                )
            } else if (selectedTab == HubTab.SETTINGS) {
                SettingsScreen(
                    onOpenWebLogin = { platform ->
                        viewModel.setWebUrl(platform.webLoginUrl)
                        viewModel.toggleWebMode(true)
                    },
                    onShowMessage = { msg -> viewModel.showSnackbar(msg) },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Native Client Mode
                Column(modifier = Modifier.fillMaxSize()) {
                    // Story Row for Messenger / All
                    if (selectedTab == HubTab.MESSENGER || selectedTab == HubTab.ALL) {
                        StoryRow(
                            stories = activeStories,
                            onAddStoryClick = { viewModel.showSnackbar("Opening camera to add story...") },
                            onStoryClick = { story -> viewModel.showSnackbar("Viewing story by ${story.name}") }
                        )
                    }

                    // Filter Chips (All, Unread, Starred)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = filter == ConversationFilter.ALL,
                            onClick = { viewModel.setFilter(ConversationFilter.ALL) },
                            label = { Text("All") },
                            modifier = Modifier.testTag("filter_all")
                        )
                        FilterChip(
                            selected = filter == ConversationFilter.UNREAD,
                            onClick = { viewModel.setFilter(ConversationFilter.UNREAD) },
                            label = { Text("Unread") },
                            modifier = Modifier.testTag("filter_unread")
                        )
                        FilterChip(
                            selected = filter == ConversationFilter.STARRED,
                            onClick = { viewModel.setFilter(ConversationFilter.STARRED) },
                            label = { Text("Starred ⭐") },
                            modifier = Modifier.testTag("filter_starred")
                        )
                    }

                    // Conversation / Email List
                    if (conversations.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "No conversations found",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Tap + to start a new chat or email",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("conversations_list"),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(conversations, key = { it.id }) { conv ->
                                ConversationItem(
                                    conversation = conv,
                                    onClick = { viewModel.openConversation(conv.id) },
                                    onToggleStar = { viewModel.toggleStar(conv.id) },
                                    showPlatformBadge = selectedTab == HubTab.ALL
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
