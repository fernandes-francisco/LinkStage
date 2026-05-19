package turmaA.grupoB.LinkStage.ui.aluno.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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

// endregion

// region Sample data

val avatarColors = listOf(LightBlue, DarkBlue, MediumBlue)

val sampleConversations = listOf(
    Conversation("1", "FR | Francisco Fernandes", "FF", "Boa pergunta.", "22:42AM", unreadCount = 1, avatarColorIndex = 0),
    Conversation("2", "Tiago Alexandre", "TA", "Como assim?", "Ontem", unreadCount = 0, avatarColorIndex = 1),
    Conversation("3", "MA | Miguel Azevedo", "MA", "Nota-se.", "2d Atrás", unreadCount = 0, avatarColorIndex = 2),
    Conversation("4", "VS | Viana S.T.Arts", "VS", "Altera a dashboard", "22:42AM", unreadCount = 0, avatarColorIndex = 0),
)

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

    val filtered = if (searchQuery.isEmpty()) conversations
    else conversations.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
            it.lastMessage.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = LightBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nova mensagem")
            }
        },
        containerColor = BackgroundLight,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            MessagesTopBar()

            Text(
                text = "Mensagens",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue,
                ),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            )

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

@Composable
fun ConversationItem(
    conversation: Conversation,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
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

// region Shared components

@Composable
private fun MessagesTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "LINK",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
            ),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            "STAGE",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Light,
                color = DarkBlue,
            ),
        )
    }
}

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