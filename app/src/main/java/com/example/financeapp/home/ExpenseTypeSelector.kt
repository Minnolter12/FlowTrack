package com.example.financeapp.home

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.financeapp.data.ExpenseItemEntity
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.example.financeapp.AppViewModel
import kotlinx.coroutines.launch

@OptIn (ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTypeSelector(
    navController: NavController,
    viewModel: AppViewModel
) {
    val haptic = LocalHapticFeedback.current
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val categories by viewModel.getCategories.collectAsState()
    val allItems by viewModel.expenseItems.collectAsState()

    val groupedData = remember(categories, allItems) {
        categories.map { categoryName ->
            categoryName to allItems.filter { it.category == categoryName }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .padding(bottom = 84.dp, end = 16.dp)
                    .combinedClickable(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                            // navController.navigate("your_route")
                        },
                        onLongClick = {
                            scope.launch {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                snackBarHostState.showSnackbar(
                                    message = "Add Category",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
            ) {
                FloatingActionButton(
                    onClick = { },
                    shape = FloatingActionButtonDefaults.shape,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Where did you spend?",
                    fontWeight = FontWeight.Medium,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(groupedData, key = { it.first }) { (categoryName, itemsInCategory)  ->

                // Always show the Category Header
                Text(
                    text = categoryName,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    if (itemsInCategory.isNotEmpty()) {
                        // Show the actual items
                        items(itemsInCategory, key = { it.id }) { item ->
                            BigSquareCard(
                                label = item.title,
                                icon = Icons.Rounded.Payments,
                                onClick = {
                                    scope.launch {
                                        haptic.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
                                    }
                                }
                            )
                        }
                    }

                    else {
                        // Show the empty state ONLY for this specific category
                        item {
                            Text(
                                text = "No items in $categoryName",
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}