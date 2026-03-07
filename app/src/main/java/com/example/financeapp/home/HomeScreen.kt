package com.example.financeapp.home

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.LocalGroceryStore
import androidx.compose.material.icons.rounded.Payment
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.financeapp.AppViewModel
import com.example.financeapp.data.ExpenseEntity
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel,
    appViewModel: AppViewModel,
    navController: NavController
) {
    val uiState by homeScreenViewModel.uiState.collectAsState()
    val haptic = LocalHapticFeedback.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val total by appViewModel.totalExpense.collectAsState()
    val recentExpenses by appViewModel.recentExpenses.collectAsState()



    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Add Expense") },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add Expense"
                    )
                },
                onClick = {
                    scope.launch {
                        navController.navigate("keyboardScreen")
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                },
                modifier = Modifier.padding(
                    bottom = 84.dp,
                    end = 16.dp
                ),
                expanded = true,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            )
        }

    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            val formattedDate = currentDate.format(formatter)

            item {
                GlobalText(
                    text = formattedDate,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .clickable(
                            onClick = {

                            }
                        )
                )
            }

            item {
                Spacer(modifier = Modifier.height(5.dp))
            }

            when (val state = uiState) {
                is HomeScreenUiState.Result -> {
                    item {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontSize = 50.sp)) {
                                    append("₹")
                                }
                                append("${total}")
                            },
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 70.sp
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                is HomeScreenUiState.Error -> {
                    val message = state.message
                    item {
                        LaunchedEffect(snackbarHostState, message) {
                            if (message != null) {
                                snackbarHostState.showSnackbar(message)
                            }
                        }
                    }
                }
            }
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    item {
                        ActionIconButton(
                            textOne = "Cash",
                            textTwo = "Hello",
                            icon = Icons.Rounded.Payments,
                            onClick = {
                                // Action for viewing cards
                            }
                        )
                    }
                    item {
                        ActionIconButton(
                            textOne = "UPI Balance",
                            textTwo = "Hello",
                            icon = Icons.Rounded.CreditCard,
                            onClick = {
                                // Action
                            }
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                WideExpenseHistoryCard(
                    onClick = {
                        haptic.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.LongPress)
                    },
                    recentExpenses = recentExpenses
                )
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                WideSquareCard(
                    label = "Add Points",
                    icon = Icons.Rounded.Payment,
                ) {}
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                WideSquareCard(
                    label = "Add Points",
                    icon = Icons.Rounded.Payment,
                ) {}
            }
        }
    }
}

@Composable
fun KeyboardScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    appViewModel: AppViewModel
) {
    val haptic = LocalHapticFeedback.current
    var input by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) { data ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentSize(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.inverseSurface
                )
            ) {
                Text(
                    text = data.visuals.message,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }

        } },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingvalues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingvalues)
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontSize = 50.sp)) {
                            append("₹")
                        }
                        append(if (input.isEmpty()) "0" else input)
                    },
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 80.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    CustomKeyboardButton(modifier = Modifier.weight(1f), symbol = "7", onClick = { input += "7" })
                    CustomKeyboardButton(modifier = Modifier.weight(1f), symbol = "8", onClick = { input += "8" })
                    CustomKeyboardButton(modifier = Modifier.weight(1f), symbol = "9", onClick = { input += "9" })
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    CustomKeyboardButton(modifier = Modifier.weight(1f), symbol = "4", onClick = { input += "4" })
                    CustomKeyboardButton(modifier = Modifier.weight(1f), symbol = "5", onClick = { input += "5" })
                    CustomKeyboardButton(modifier = Modifier.weight(1f), symbol = "6", onClick = { input += "6" })
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    CustomKeyboardButton(modifier = Modifier.weight(1f), symbol = "1", onClick = { input += "1" })
                    CustomKeyboardButton(modifier = Modifier.weight(1f), symbol = "2", onClick = { input += "2" })
                    CustomKeyboardButton(modifier = Modifier.weight(1f), symbol = "3", onClick = { input += "3" })
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Spacer(modifier = Modifier.weight(2f))
                    CustomKeyboardButton(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.ArrowBackIosNew,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                            navController.popBackStack()
                        }
                    )
                    CustomKeyboardButton(
                        modifier = Modifier.weight(1f),
                        icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        onClick = {
                            if (input.isEmpty()) {
                                scope.launch {
                                    val job = launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Please enter an amount",
                                            duration = SnackbarDuration.Indefinite
                                        )
                                    }
                                    delay(1500L)
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    job.cancel()

                                }
                                return@CustomKeyboardButton
                            }
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                      //      appViewModel.addExpense("Expense", amount = input.toInt())
                            //      implement this inside expenseTypeSelector
                            navController.navigate("expenseTypeSelector")
                        }
                    )
                    CustomKeyboardButton(
                        modifier = Modifier.weight(1f),
                        icon = Icons.AutoMirrored.Filled.Backspace,
                        onClick = {
                            if (input.isNotEmpty()) {
                                input = input.dropLast(1)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GlobalButton(
    modifier: Modifier,
    onClick: () -> Unit,
    name: String
) {
    Button(
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(
            text = name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun GlobalText(
    text: String?,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    Text(
        text = text ?: "",
        style = style,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
fun ActionIconButton(
    modifier: Modifier = Modifier,
    textOne: String,
    textTwo: String,
    icon: ImageVector,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = containerColor,
        modifier = modifier
            .padding(10.dp)
            .width(170.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 6.dp, end = 16.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = contentColor
                )
            }
            Column {
                Text(
                    text = textOne,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                    maxLines = 15
                )
                Text(
                    text = textTwo,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Light,
                    color = contentColor,
                    maxLines = 1
                )
            }

        }
    }
}

@Composable
private fun border(width: Dp, color: Color, shape: Shape) = Modifier.border(width, color, shape)

@Composable
fun CustomKeyboardButton(
    modifier: Modifier = Modifier,
    symbol: String? = null,
    icon: ImageVector? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(6.dp)
            .clip(CircleShape)
            .background(containerColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (symbol != null) {
            Text(
                text = symbol,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium,
                color = contentColor
            )
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = contentColor
            )
        }
    }
}

@Composable
fun BigSquareCard(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .size(160.dp)
            .aspectRatio(1f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )
        }
    }
}

@Composable
fun WideSquareCard(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(150.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )
        }
    }
}

@Composable
fun WideExpenseHistoryCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    recentExpenses: List<ExpenseEntity>
) {
    val haptic = LocalHapticFeedback.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(150.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        )
    ) {
        Row(
            modifier = modifier.fillMaxSize().padding(8.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.width(90.dp).fillMaxHeight(),
                shape = RoundedCornerShape(
                    topEnd = 8.dp, topStart = 24.dp, bottomEnd = 8.dp, bottomStart = 24.dp
                ),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Column(
                    modifier = modifier.fillMaxSize().padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.History,
                        contentDescription = "Expense History",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(45.dp)
                    )
                }
            }

            Spacer(modifier = modifier.width(10.dp))

            ElevatedCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                shape = RoundedCornerShape(
                    topEnd = 24.dp, topStart = 8.dp, bottomEnd = 24.dp, bottomStart = 8.dp
                ),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                LazyRow() {
                   if (recentExpenses.isEmpty()) {
                       item {
                           Box(
                               modifier = Modifier.padding(4.dp).fillParentMaxSize(),
                               contentAlignment = Alignment.Center

                           ) {
                               Text(
                                   text = "No Expenses yet!",
                                   style = MaterialTheme.typography.titleLarge,
                                   modifier = Modifier.align(Alignment.Center)
                               )
                           }
                       }
                   } else {
                       items(recentExpenses) { expense ->
                           Row(
                               modifier = Modifier.padding(4.dp).fillMaxWidth(),
                               horizontalArrangement = Arrangement.SpaceBetween,
                               verticalAlignment = Alignment.CenterVertically
                           ) {
                               Text(
                                   text = expense.title,
                                   style = MaterialTheme.typography.titleMedium
                               )

                               Spacer(modifier = modifier.width(4.dp))

                               Text(
                                   text = "₹${expense.amount}",
                                   style = MaterialTheme.typography.titleMedium,
                               )
                           }
                       }
                   }
                }
            }
        }
    }
}
