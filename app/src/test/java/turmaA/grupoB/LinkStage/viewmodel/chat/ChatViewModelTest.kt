package turmaA.grupoB.LinkStage.viewmodel.chat

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import turmaA.grupoB.LinkStage.ui.aluno.chat.ChatMessage
import turmaA.grupoB.LinkStage.ui.aluno.chat.Contact
import turmaA.grupoB.LinkStage.ui.aluno.chat.Conversation

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    @Test
    fun loadConversationsShouldPublishSuccessWithConversationsAndContacts() = runTest {
        val dataSource = FakeChatDataSource(
            conversations = listOf(sampleConversation("thread-1", "Student")),
            contacts = listOf(sampleContact("student-1", "Student")),
        )
        val viewModel = ChatViewModel(dataSource, StandardTestDispatcher(testScheduler))

        viewModel.loadConversations()
        advanceUntilIdle()

        val state = viewModel.chatUiState.value
        assertTrue(state is ChatUiState.Success)
        assertEquals(listOf(sampleConversation("thread-1", "Student")), (state as ChatUiState.Success).conversations)
        assertEquals(listOf(sampleContact("student-1", "Student")), state.contacts)
    }

    @Test
    fun loadConversationsShouldPublishEmptyWhenDataSourceReturnsNoConversations() = runTest {
        val viewModel = ChatViewModel(FakeChatDataSource(), StandardTestDispatcher(testScheduler))

        viewModel.loadConversations()
        advanceUntilIdle()

        assertTrue(viewModel.chatUiState.value is ChatUiState.Empty)
    }

    @Test
    fun loadConversationsShouldPublishErrorWhenDataSourceThrows() = runTest {
        val viewModel = ChatViewModel(
            FakeChatDataSource(loadConversationsError = IllegalStateException("No institution")),
            StandardTestDispatcher(testScheduler),
        )

        viewModel.loadConversations()
        advanceUntilIdle()

        val state = viewModel.chatUiState.value
        assertTrue(state is ChatUiState.Error)
        assertEquals("No institution", (state as ChatUiState.Error).message)
    }

    @Test
    fun sendMessageShouldOptimisticallyAppendMessageThenKeepSuccessAfterRepositoryCall() = runTest {
        val viewModel = ChatViewModel(FakeChatDataSource(), StandardTestDispatcher(testScheduler))
        viewModel.loadThreadMessages("thread-1")
        advanceUntilIdle()

        viewModel.sendMessage("thread-1", "Hello")
        advanceUntilIdle()

        val state = viewModel.threadMessagesUiState.value
        assertTrue(state is ThreadMessagesUiState.Success)
        assertEquals("Hello", (state as ThreadMessagesUiState.Success).messages.last().text)
        assertTrue(state.messages.last().isSentByMe)
    }

    @Test
    fun sendMessageShouldRefreshConversationsAfterRepositoryCall() = runTest {
        val refreshConversation = sampleConversation("thread-1", "Student", "Hello")
        val dataSource = FakeChatDataSource(
            conversations = listOf(refreshConversation),
            contacts = listOf(sampleContact("student-1", "Student")),
            threadMessagesByThread = mapOf("thread-1" to listOf(sampleMessage("server", "Hello", false))),
        )
        val viewModel = ChatViewModel(dataSource, StandardTestDispatcher(testScheduler))

        viewModel.sendMessage("thread-1", "Hello")
        advanceUntilIdle()

        val state = viewModel.chatUiState.value
        assertTrue(state is ChatUiState.Success)
        assertEquals(listOf(refreshConversation), (state as ChatUiState.Success).conversations)
        assertEquals(1, dataSource.loadConversationsCount)
    }

    @Test
    fun sendMessageShouldKeepThreadMessagesVisibleWhenRepositoryThrows() = runTest {
        val existingMessage = sampleMessage("server", "Existing", false)
        val dataSource = FakeChatDataSource(
            sentError = IllegalStateException("Send failed"),
            threadMessagesByThread = mapOf("thread-1" to listOf(existingMessage)),
        )
        val viewModel = ChatViewModel(dataSource, StandardTestDispatcher(testScheduler))
        viewModel.loadThreadMessages("thread-1")
        advanceUntilIdle()

        viewModel.sendMessage("thread-1", "Hello")
        advanceUntilIdle()

        val state = viewModel.threadMessagesUiState.value
        assertTrue(state is ThreadMessagesUiState.Success)
        assertEquals(listOf(existingMessage), (state as ThreadMessagesUiState.Success).messages)
    }

    @Test
    fun loadThreadMessagesShouldKeepPreviousMessagesWhenReloadReturnsEmpty() = runTest {
        val existingMessage = sampleMessage("server", "Existing", false)
        val viewModel = ChatViewModel(
            FakeChatDataSource(threadMessagesByThread = mapOf("thread-1" to listOf(existingMessage))),
            StandardTestDispatcher(testScheduler),
        )
        viewModel.loadThreadMessages("thread-1")
        advanceUntilIdle()

        viewModel.loadThreadMessages("thread-1")
        advanceUntilIdle()

        val state = viewModel.threadMessagesUiState.value
        assertTrue(state is ThreadMessagesUiState.Success)
        assertEquals(listOf(existingMessage), (state as ThreadMessagesUiState.Success).messages)
    }

    @Test
    fun loadThreadMessagesShouldPublishEmptyWhenInitialDataSourceReturnsNoMessages() = runTest {
        val viewModel = ChatViewModel(FakeChatDataSource(), StandardTestDispatcher(testScheduler))

        viewModel.loadThreadMessages("thread-1")
        advanceUntilIdle()

        assertTrue(viewModel.threadMessagesUiState.value is ThreadMessagesUiState.Empty)
    }

    private fun sampleConversation(id: String, name: String, lastMessage: String = "Last"): Conversation = Conversation(
        id = id,
        name = name,
        initials = name.take(2),
        lastMessage = "Last",
        time = "10:00",
        unreadCount = 0,
        avatarColorIndex = 0,
    )

    private fun sampleContact(id: String, name: String): Contact = Contact(
        id = id,
        name = name,
        role = "Student",
        initials = name.take(2),
        avatarColorIndex = 0,
    )

    private fun sampleMessage(id: String, text: String, isSentByMe: Boolean): ChatMessage = ChatMessage(
        id = id,
        text = text,
        isSentByMe = isSentByMe,
        time = "10:00",
    )

    private class FakeChatDataSource(
        private val conversations: List<Conversation> = emptyList(),
        private val contacts: List<Contact> = emptyList(),
        private val loadConversationsError: Throwable? = null,
        private val sentError: Throwable? = null,
        private val threadMessagesByThread: Map<String, List<ChatMessage>> = emptyMap(),
        private val resolveResult: EnsureThreadResult? = null,
    ) : ChatDataSource {
        var loadConversationsCount = 0

        override suspend fun loadConversations(): ChatUiState.Success {
            loadConversationsCount += 1
            loadConversationsError?.let { throw it }
            return ChatUiState.Success(conversations, contacts)
        }

        override suspend fun loadThreadMessages(threadId: String): List<ChatMessage> = threadMessagesByThread[threadId].orEmpty()

        override suspend fun sendMessage(threadId: String, text: String) {
            sentError?.let { throw it }
        }

        override suspend fun markThreadMessagesAsRead(threadId: String) = Unit

        override suspend fun ensureThreadForStudent(studentId: String): EnsureThreadResult? = resolveResult

        override fun resolveThreadIdForStudent(studentId: String): String? = null
    }

    @Test
    fun ensureThreadForStudentShouldDelegateToDataSource() = runTest {
        val expected = EnsureThreadResult("thread-1", created = false)
        val dataSource = FakeChatDataSource(resolveResult = expected)
        val viewModel = ChatViewModel(dataSource, StandardTestDispatcher(testScheduler))

        viewModel.ensureThreadForStudent("student-1")
        advanceUntilIdle()

        assertEquals(expected, viewModel.ensureThreadResult.value)
    }

    @Test
    fun ensureThreadForStudentShouldPublishNullWhenDataSourceThrows() = runTest {
        val dataSource = FakeChatDataSource(
            loadConversationsError = IllegalStateException("No institution"),
            resolveResult = null,
        )
        val viewModel = ChatViewModel(dataSource, StandardTestDispatcher(testScheduler))

        viewModel.ensureThreadForStudent("student-1")
        advanceUntilIdle()

        assertEquals(null, viewModel.ensureThreadResult.value)
    }
}
