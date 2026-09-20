package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.PlatformType

@Composable
fun ComposeDialog(
    initialPlatform: PlatformType,
    onDismiss: () -> Unit,
    onSend: (platform: PlatformType, recipient: String, subject: String?, body: String) -> Unit
) {
    var selectedPlatform by remember { mutableStateOf(initialPlatform) }
    var recipient by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("compose_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedPlatform.isEmailService) "Compose Email" else "New Message",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Platform selector pills
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PlatformType.values().forEach { platform ->
                        FilterChip(
                            selected = selectedPlatform == platform,
                            onClick = { selectedPlatform = platform },
                            label = {
                                Text("${platform.iconEmoji} ${platform.shortName}", fontSize = 12.sp)
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = platform.brandColor.copy(alpha = 0.2f),
                                selectedLabelColor = platform.brandColor
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Recipient field
                OutlinedTextField(
                    value = recipient,
                    onValueChange = { recipient = it },
                    label = {
                        Text(
                            when (selectedPlatform) {
                                PlatformType.MESSENGER -> "Recipient Name"
                                PlatformType.TWITTER_X -> "@username or Name"
                                PlatformType.GMAIL, PlatformType.YAHOO_MAIL -> "To (Email address)"
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("compose_recipient_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // Subject field for emails
                if (selectedPlatform.isEmailService) {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("Subject") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("compose_subject_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Message body
                OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    label = {
                        Text(if (selectedPlatform.isEmailService) "Compose email body" else "Message text")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .testTag("compose_body_input"),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 6
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Send Button
                Button(
                    onClick = {
                        onSend(
                            selectedPlatform,
                            recipient,
                            if (selectedPlatform.isEmailService) subject else null,
                            body
                        )
                    },
                    enabled = recipient.isNotBlank() && body.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("compose_send_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = selectedPlatform.brandColor)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Send via ${selectedPlatform.shortName}",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
