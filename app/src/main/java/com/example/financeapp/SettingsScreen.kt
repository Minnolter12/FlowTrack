package com.example.financeapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun SettingsScreen(
    appViewModel: AppViewModel, navController: NavController
) {
    val isDarkTheme by appViewModel.isDarkTheme.collectAsState()
    val haptic = LocalHapticFeedback.current

    val currentTheme = if (isDarkTheme) {
        "Light Theme"
    } else {
        "Dark Theme"
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(), // Added to enable scrolling
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp) // Fixed: Use top and bottom separately
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .clickable(
                        role = Role.Switch,
                        onClick = {
                            appViewModel.toggleAppTheme()
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    )
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Brush,
                    contentDescription = "Theme Icon"
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = currentTheme,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = null
                )
            }
        }

        item {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
        }

        // Using repeat to ensure there is enough content to scroll
        repeat(20) { index ->
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .clickable(onClick = { /* TODO */ })
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Setting Item ${index + 1}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.weight(1f))

                    val currentDate = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                    val formattedDate = currentDate.format(formatter)

                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            item {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
            }
        }
    }
}
