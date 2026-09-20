package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActiveStory
import com.example.ui.theme.MessengerBlue
import com.example.ui.theme.MessengerGradientEnd
import com.example.ui.theme.MessengerGradientStart

@Composable
fun StoryRow(
    stories: List<ActiveStory>,
    onAddStoryClick: () -> Unit,
    onStoryClick: (ActiveStory) -> Unit,
    modifier: Modifier = Modifier
) {
    val storyBrush = Brush.linearGradient(
        colors = listOf(MessengerGradientStart, MessengerBlue, MessengerGradientEnd)
    )

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .testTag("messenger_stories_row"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // "Your Story / Add Story" item
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(62.dp)
                    .clickable { onAddStoryClick() }
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Story",
                        tint = MessengerBlue,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Text(
                    text = "Your story",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Active friend stories
        items(stories, key = { it.id }) { story ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(62.dp)
                    .clickable { onStoryClick(story) }
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .border(2.dp, storyBrush, CircleShape)
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    UserAvatar(
                        initials = story.avatarInitials,
                        avatarColor = story.avatarColor,
                        size = 46.dp,
                        isOnline = story.isOnline
                    )
                }
                Text(
                    text = story.name,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
