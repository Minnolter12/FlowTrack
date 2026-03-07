package com.example.financeapp.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocalGroceryStore
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
import com.example.financeapp.AppViewModel

@Deprecated(level = DeprecationLevel.WARNING, message = "Review Logic inside card composable." +
        " Card Logic when " +
        "clicked goes to homescreen, fix it by adding a new function to it")
@Composable
fun ExpenseTypeSelector(
    navController: NavController,
    appViewModel: AppViewModel
) {
    val haptic = LocalHapticFeedback.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
            item {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }
            item {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Household",
                        fontWeight = FontWeight.Medium,
                        fontSize = 24.sp,
                    )
                }
            }
            item {
                LazyRow(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    item {
                        BigSquareCard(
                            "Grocery",
                            Icons.Rounded.LocalGroceryStore,
                            onClick = {
                                navController.navigate("homescreen")
                            }
                        )
                    }
                    item {
                        BigSquareCard(
                            "Tools",
                            Icons.Rounded.LocalGroceryStore,
                            onClick = {
                                navController.navigate("homescreen")
                            }
                        )
                    }
                    item {
                        BigSquareCard(
                            "Amazon",
                            Icons.Rounded.LocalGroceryStore,
                            onClick = {
                                navController.navigate("homescreen")  //test
                            }
                        )
                    }
                }
            }
            item {
                Spacer(
                    modifier = Modifier.height(23.dp)
                )
            }
            item {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }
            item {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "School",
                        fontWeight = FontWeight.Medium,
                        fontSize = 24.sp,
                    )
                }
            }
            item {
                LazyRow(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    item {
                        BigSquareCard(
                            "Canteen",
                            Icons.Rounded.LocalGroceryStore,
                            onClick = {
                                navController.navigate("homescreen")
                            }
                        )
                    }
                }
            }
            item {
                Spacer(
                    modifier = Modifier.height(23.dp)
                )
            }
            item {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }
            item {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "School",
                        fontWeight = FontWeight.Medium,
                        fontSize = 24.sp,
                    )
                }
            }
            item {
                LazyRow(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    item {
                        BigSquareCard(
                            "Canteen",
                            Icons.Rounded.LocalGroceryStore,
                            onClick = {
                                navController.navigate("homescreen")
                            }
                        )
                    }
                }
            }
        }
    }
}