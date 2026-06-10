package turmaA.grupoB.LinkStage.ui.aluno.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import turmaA.grupoB.LinkStage.ui.common.CommonTopBar
import turmaA.grupoB.LinkStage.ui.common.LinkStageDialog
import turmaA.grupoB.LinkStage.ui.common.LinkStageLogo
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.MediumBlue

// region Data models

data class Conversation(
    val id: String,
    val name: String,
    val initials: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
    val avatarColorIndex: Int = 0,
)

data class Contact(
    val id: String,
    val name: String,
    val role: String,
    val initials: String,
    val avatarColorIndex: Int,
)

// endregion

// region Sample data

val avatarColors = listOf(LightBlue, DarkBlue, MediumBlue)

val sampleConversations = listOf(
    Conversation("1", "FR | Francisco Fernandes", "FF", "Boa pergunta.", "22:42AM", unreadCount = 1, avatarColorIndex = 0),
    Conversation("2", "Tiago Alexandre", "TA", "Como assim?", "Ontem", unreadCount = 0, avatarColorIndex = 1),
    Conversation("3", "MA | Miguel Azevedo", "MA", "Nota-se.", "2d Atrás", unreadCount = 0, avatarColorIndex = 2),
    Conversation("4", "VS | Viana S.T.Arts", "VS", "Altera a dashboard", "22:42AM", unreadCount = 0, avatarColorIndex = 0),
)

val sampleContacts = listOf(
    Contact("s1", "Tiago Rodrigues", "Estudante", "TR", 0),
    Contact("s2", "Francisco Fernandes", "Estudante", "FF", 1),
    Contact("13", "Ana Silva", "Gestora de Projeto", "AS", 0),
    Contact("10", "Francisco Fernandes", "Orientador Instituição", "FF", 0),
    Contact("14", "José Santos", "Tutor Técnico", "JS", 1),
    Contact("12", "Miguel Azevedo", "Responsável RH", "MA", 2),
    Contact("11", "Tiago Alexandre", "Orientador Empresa", "TA", 1),
).sortedBy { it.name }

// endregion

// region Entry point

@Composable
fun ChatAlunoScreen(
    onOpenChat: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    MessagesListScreen(
        conversations = sampleConversations,
        onOpenChat = onOpenChat,
        modifier = modifier,
    )
}

// endregion

// region Messages list

@Composable
private fun MessagesListScreen(
    conversations: List<Conversation>,
    onOpenChat: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showNewMessageModal by rememberSaveable { mutableStateOf(false) }
    var currentConversations by remember { mutableStateOf(conversations) }
    var conversationToDelete by remember { mutableStateOf<Conversation?>(null) }

    val filtered = if (searchQuery.isEmpty()) currentConversations
    else currentConversations.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
            it.lastMessage.contains(searchQuery, ignoreCase = true)
    }

    if (showNewMessageModal) {
        NewMessageModal(
            onDismiss = { showNewMessageModal = false },
            onContactSelected = { contactId ->
                showNewMessageModal = false
                onOpenChat(contactId)
            }
        )
    }

    if (conversationToDelete != null) {
        LinkStageDialog(
            title = "Apagar Conversa",
            onConfirm = {
                currentConversations = currentConversations.filter { it.id != conversationToDelete!!.id }
                conversationToDelete = null
            },
            onDismiss = { conversationToDelete = null },
            confirmText = "Apagar",
            dismissText = "Cancelar",
            content = {
                Text(
                    text = "Tens a certeza que pretendes apagar a conversa com ${conversationToDelete!!.name}?",
                    color = DarkGrey,
                    lineHeight = 22.sp,
                )
            }
        )
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewMessageModal = true },
                containerColor = LightBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nova mensagem")
            }
        },
        topBar = {
            Column(modifier = Modifier.background(BackgroundLight)) {
                CommonTopBar()
                Text(
                    text = "Mensagens",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue,
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                )
            }
        },
        containerColor = BackgroundLight,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            MessagesSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filtered, key = { it.id }) { conversation ->
                    ConversationItem(
                        conversation = conversation,
                        onClick = { onOpenChat(conversation.id) },
                        onLongClick = { conversationToDelete = conversation }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = BorderGrey,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConversationItem(
    conversation: Conversation,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
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

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = conversation.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = conversation.lastMessage,
                style = MaterialTheme.typography.bodySmall,
                color = if (conversation.unreadCount > 0) DarkBlue else DarkGrey,
                fontWeight = if (conversation.unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = conversation.time,
                style = MaterialTheme.typography.labelSmall,
                color = if (conversation.unreadCount > 0) LightBlue else DarkGrey,
            )
            if (conversation.unreadCount > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(LightBlue),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = conversation.unreadCount.toString(),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    )
                }
            }
        }
    }
}

// endregion

@Composable
private fun NewMessageModal(
    onDismiss: () -> Unit,
    onContactSelected: (String) -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }
    val filteredContacts = sampleContacts.filter {
        it.name.contains(query, ignoreCase = true) || it.role.contains(query, ignoreCase = true)
    }

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(500.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nova Mensagem",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue
                        )
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = DarkGrey)
                    }
                }

                // Search
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    placeholder = { Text("Pesquisar contactos...", color = DarkGrey, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Outlined.Search, null, tint = DarkGrey) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LightBlue,
                        unfocusedBorderColor = BorderGrey,
                        focusedContainerColor = BackgroundLight.copy(alpha = 0.5f),
                        unfocusedContainerColor = BackgroundLight.copy(alpha = 0.5f),
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Contacts List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(filteredContacts) { contact ->
                        ContactItem(
                            contact = contact,
                            onClick = { onContactSelected(contact.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactItem(
    contact: Contact,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(avatarColors[contact.avatarColorIndex % avatarColors.size]),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = contact.initials,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = DarkBlue
            )
            Text(
                text = contact.role,
                style = MaterialTheme.typography.labelSmall,
                color = DarkGrey
            )
        }
    }
}

// region Shared components

@Composable
private fun MessagesSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        placeholder = { Text("Pesquisar...", color = DarkGrey) },
        leadingIcon = {
            Icon(Icons.Outlined.Search, contentDescription = "Pesquisar", tint = DarkGrey)
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = DarkBlue,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
        ),
        singleLine = true,
    )
}

// endregion