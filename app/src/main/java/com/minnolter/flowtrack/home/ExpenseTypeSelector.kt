package com.minnolter.flowtrack.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Flight
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalCafe
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Redeem
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material.icons.rounded.Subscriptions
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.LocalLaundryService
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.VideogameAsset
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.minnolter.flowtrack.app.AppViewModel

@OptIn (ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTypeSelector(
    navController: NavController,
    viewModel: AppViewModel,
    amount: Int,
    getIconByName: (String) -> ImageVector
) {
    val haptic = LocalHapticFeedback.current
    val snackBarHostState = remember { SnackbarHostState() }

    val categories by viewModel.getCategories.collectAsState()
    val allItems by viewModel.expenseItems.collectAsState()

    val groupedData = remember(categories, allItems) {
        categories.map { categoryName ->
            categoryName to allItems.filter { it.category == categoryName }
        }
    }

    var showCategorySheet by remember { mutableStateOf(false) }
    var showItemSheetForCategory by remember { mutableStateOf<String?>(null) }
    var showIconEditor by remember { mutableStateOf<String?>(null) }

    val iconColors = listOf(
        Color(0xFFE57373), Color(0xFF81C784), Color(0xFF64B5F6),
        Color(0xFFFFB74D), Color(0xFFBA68C8), Color(0xFF4DB6AC)
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                     haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                     showCategorySheet = true
                },
                modifier = Modifier.padding(bottom = 84.dp, end = 16.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { paddingValues ->
        if (categories.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Hmm...It's Lonely in Here",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Use the + button to add your first category",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                item {
                    Text(
                        text = "Where did you spend?",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(24.dp)
                    )
                }

                itemsIndexed(groupedData) { index, (categoryName, itemsInCategory)  ->
                    val bgColor = iconColors[index % iconColors.size]
                    
                    Column(modifier = Modifier.padding(vertical = 16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth().padding(horizontal = 23.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(60.dp),
                                shape = CircleShape,
                                color = bgColor.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, bgColor.copy(alpha = 0.5f)),
                                onClick = {
                                    showIconEditor = categoryName
                                }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = getIconByName(itemsInCategory.firstOrNull()?.icon ?: Icons.Rounded.Add.name),
                                        contentDescription = null,
                                        tint = bgColor,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }
                            
                            Text(
                                text = categoryName,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp)
                        ) {
                            if (itemsInCategory.isNotEmpty()) {
                                items(itemsInCategory) { item ->
                                    if (item.title != "" ) {
                                        BigSquareCard(
                                            label = item.title,
                                            icon = getIconByName(item.icon),
                                            onClick = {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                // SAVE EXPENSE
                                                viewModel.addExpense(title = item.title, amount = amount)
                                                // GO HOME
                                                navController.navigate("homeScreen") {
                                                    popUpTo("homeScreen") { inclusive = true }
                                                }
                                            }
                                        )
                                    }

                                }
                                // Always show a card to add a new item to this category
                                item {
                                    BigSquareCard(
                                        label = "Add Item",
                                        icon = Icons.Rounded.Add,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            showItemSheetForCategory = categoryName
                                        }
                                    )
                                }
                            } else {
                                item {
                                    BigSquareCard(
                                        label = "Add Item",
                                        icon = Icons.Rounded.Add,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            showItemSheetForCategory = categoryName
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showCategorySheet) {
            AddCategoryOrItemsSheet(
                type = "Category",
                onDismiss = { showCategorySheet = false },
                onConfirm = { result ->
                    viewModel.addItem(
                        name = "",
                        category = result,
                        icon = Icons.Rounded.Add.name
                    )
                    showCategorySheet = false
                },
            )
        }

        if (showItemSheetForCategory != null) {
            val category = showItemSheetForCategory!!
            AddCategoryOrItemsSheet(
                type = "Item",
                onDismiss = { showItemSheetForCategory = null },
                onConfirm = { name ->
                    viewModel.addItem(name = name, category = category, icon = Icons.Rounded.Payments.name)
                    showItemSheetForCategory = null
                }
            )
        }

        if (showIconEditor !=  null) {
            val category = showIconEditor!!
            IconEditorSheet(
                categoryName = category,
                onDismiss = { showIconEditor = null },
                onConfirm = { selectedIcon ->
                    viewModel.updateCategoryIcon(category, selectedIcon.name)
                    showIconEditor = null
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryOrItemsSheet(
    type: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Add New $type",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { if (text.isNotBlank()) onConfirm(text) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirm")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconEditorSheet(
    categoryName: String,
    onDismiss: () -> Unit,
    onConfirm: (ImageVector) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val haptic = LocalHapticFeedback.current

    // Define a list of colors to rotate through for variety
    val iconColors = listOf(
        Color(0xFFE57373), Color(0xFF81C784), Color(0xFF64B5F6),
        Color(0xFFFFB74D), Color(0xFFBA68C8), Color(0xFF4DB6AC)
    )

    val categoryIcons = listOf(
        Icons.Rounded.Restaurant, Icons.Rounded.ShoppingCart,
        Icons.Rounded.DirectionsCar, Icons.Rounded.Home,
        Icons.Rounded.Bolt, Icons.Rounded.Movie,
        Icons.Rounded.Store, Icons.Rounded.MedicalServices,
        Icons.Rounded.Payments, Icons.Rounded.School,
        Icons.Rounded.Pets, Icons.Rounded.Subscriptions,
        Icons.Rounded.Redeem, Icons.Rounded.Flight,
        Icons.Rounded.FitnessCenter, Icons.Rounded.LocalCafe,
        Icons.Rounded.DirectionsBus, Icons.Rounded.LocalLaundryService,
        Icons.Rounded.SelfImprovement, Icons.Rounded.VideogameAsset
    )

    var iconSelected by remember { mutableStateOf<ImageVector?>(null)}

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Choose Icon for $categoryName",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().height(350.dp)
            ) {
                itemsIndexed(categoryIcons) { index, icon ->
                    val bgColor = iconColors[index % iconColors.size]

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                iconSelected = icon
                            }
                            .padding(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(60.dp),
                            shape = CircleShape,
                            color = if (iconSelected == icon) MaterialTheme.colorScheme.primary
                            else bgColor.copy(alpha = 0.1f),
                            border = if (iconSelected == icon) null
                            else BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (iconSelected == icon) colorScheme.onPrimary else bgColor,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }
                }
            }
           // Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    onConfirm(iconSelected!!)
                },
                enabled = iconSelected != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirm")
            }
        }
    }
}

@Composable
fun BigSquareCard(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(100.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
