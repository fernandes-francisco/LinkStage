package turmaA.grupoB.LinkStage.ui.aluno.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import turmaA.grupoB.LinkStage.ui.common.LinkStageDialog
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.LightBlue

// region Data models

data class ChatMessage(
    val id: String,
    val text: String,
    val isSentByMe: Boolean,
    val time: String,
)

// endregion

@Preview(showBackground = true)
@Composable
private fun ChatScreenPreview() {
    MaterialTheme {
        ChatScreen(
            conversation = Conversation(
                id = "1",
                name = "Viana S.T.Arts",
                lastMessage = "Olá! Tens alguma dúvida?",
                time = "22:40",
                unreadCount = 2,
                initials = "V",
                avatarColorIndex = 0
            ),
            onBack = {}
        )
    }
}

// region Sample data

private val sampleMessages = mapOf(
    "1" to listOf(
        ChatMessage("m1", "Olá! Tens alguma dúvida?", false, "22:40"),
        ChatMessage("m2", "Sim, tenho uma pergunta sobre o estágio.", true, "22:41"),
        ChatMessage("m3", "Boa pergunta.", false, "22:42"),
    ),
    "2" to listOf(
        ChatMessage("m1", "Viste o email que enviei?", false, "Ontem 18:00"),
        ChatMessage("m2", "Como assim?", true, "Ontem 18:05"),
    ),
    "3" to listOf(
        ChatMessage("m1", "O relatório foi submetido.", true, "2d atrás"),
        ChatMessage("m2", "Nota-se.", false, "2d atrás"),
    ),
    "4" to listOf(
        ChatMessage("m1", "Precisamos de actualizar a dashboard.", false, "22:40"),
        ChatMessage("m2", "Altera a dashboard", true, "22:42"),
    ),
)

// endregion

// region Chat Screen

@Composable
fun ChatScreen(
    conversation: Conversation,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val initialMessages = sampleMessages[conversation.id] ?: emptyList()
    val messages = remember { mutableStateListOf(*initialMessages.toTypedArray()) }
    var inputText by remember { mutableStateOf("") }
    var searchQueries by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(false) }
    var showMuteDialog by remember { mutableStateOf(false) }

    val filteredMessages = remember(messages, searchQueries, isSearchActive) {
        if (isSearchActive && searchQueries.isNotBlank()) {
            messages.filter { it.text.contains(searchQueries, ignoreCase = true) }
        } else {
            messages
        }
    }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    fun sendMessage() {
        val text = inputText.trim()
        if (text.isEmpty()) return
        messages.add(
            ChatMessage(
                id = "new_${messages.size}",
                text = text,
                isSentByMe = true,
                time = "Agora",
            )
        )
        inputText = ""
        coroutineScope.launch {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            ChatTopBar(
                conversation = conversation,
                onBack = {
                    if (isSearchActive) {
                        isSearchActive = false
                        searchQueries = ""
                    } else {
                        onBack()
                    }
                },
                onClearHistory = { messages.clear() },
                isSearchActive = isSearchActive,
                searchQuery = searchQueries,
                onSearchQueryChange = { searchQueries = it },
                onToggleSearch = { isSearchActive = !isSearchActive },
                onMute = {
                    if (isMuted) isMuted = false
                    else showMuteDialog = true
                },
                isMuted = isMuted
            )
        },
        bottomBar = {
            if (!isSearchActive) {
                ChatInputBar(
                    text = inputText,
                    onTextChange = { inputText = it },
                    onSend = { sendMessage() },
                )
            }
        },
        containerColor = BackgroundLight,
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
        ) {
            items(filteredMessages, key = { it.id }) { message ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                ) {
                    ChatBubble(message = message)
                }
            }
        }
    }

    if (showMuteDialog) {
        MuteNotificationsDialog(
            onDismiss = { showMuteDialog = false },
            onConfirm = {
                isMuted = true
                showMuteDialog = false
            }
        )
    }
}

// endregion

// region Components

@Composable
private fun ChatTopBar(
    conversation: Conversation,
    onBack: () -> Unit,
    onClearHistory: () -> Unit = {},
    isSearchActive: Boolean = false,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onToggleSearch: () -> Unit = {},
    onMute: () -> Unit = {},
    isMuted: Boolean = false,
) {
    var showMenu by remember { mutableStateOf(false) }

    Surface(
        color = Color.White,
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = DarkBlue,
                )
            }

            if (isSearchActive) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    placeholder = { Text("Pesquisar...", color = DarkGrey) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = BackgroundLight,
                        unfocusedContainerColor = BackgroundLight,
                    ),
                    shape = RoundedCornerShape(20.dp),
                    trailingIcon = {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpar", tint = DarkGrey)
                        }
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(avatarColors[conversation.avatarColorIndex % avatarColors.size]),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = conversation.initials,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = conversation.name,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "Online",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF4CAF50),
                    )
                }
            }

            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Mais opções",
                        tint = DarkGrey,
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        text = { Text("Pesquisar na conversa") },
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        onClick = {
                            showMenu = false
                            onToggleSearch()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(if (isMuted) "Ativar notificações" else "Silenciar notificações") },
                        onClick = {
                            showMenu = false
                            onMute()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Limpar histórico de mensagens") },
                        onClick = {
                            showMenu = false
                            onClearHistory()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MuteNotificationsDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var selectedOption by remember { mutableStateOf("8 horas") }
    val options = listOf("8 horas", "1 semana", "Sempre")

    LinkStageDialog(
        onDismiss = onDismiss,
        title = "Silenciar Notificações",
        onConfirm = { onConfirm(selectedOption) },
        confirmText = "Silenciar",
        dismissText = "Cancelar",
        content = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedOption = option }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (option == selectedOption),
                            onClick = { selectedOption = option },
                            colors = RadioButtonDefaults.colors(selectedColor = DarkBlue)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option, color = DarkGrey)
                    }
                }
            }
        }
    )
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    val alignment = if (message.isSentByMe) Alignment.End else Alignment.Start
    val bubbleColor = if (message.isSentByMe) DarkBlue else Color.White
    val textColor = if (message.isSentByMe) Color.White else DarkBlue
    val shape = if (message.isSentByMe) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment,
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(shape)
                .background(bubbleColor)
                .padding(horizontal = 14.dp, vertical = 10.dp),
        ) {
            Text(
                text = message.text,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp,
            )
        }
        Text(
            text = message.time,
            style = MaterialTheme.typography.labelSmall,
            color = DarkGrey,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
        )
    }
}

@Composable
private fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
) {
    Surface(
        color = Color.White,
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        "Escreve uma mensagem...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkGrey,
                    )
                },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = BorderGrey,
                    focusedBorderColor = DarkBlue,
                    unfocusedContainerColor = BackgroundLight,
                    focusedContainerColor = BackgroundLight,
                ),
                maxLines = 4,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSend() }),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(if (text.isNotBlank()) LightBlue else DarkGrey)
                    .clickable(enabled = text.isNotBlank()) { onSend() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

// endregion
