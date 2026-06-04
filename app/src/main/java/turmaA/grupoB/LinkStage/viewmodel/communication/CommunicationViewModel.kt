package turmaA.grupoB.LinkStage.viewmodel.communication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import turmaA.grupoB.LinkStage.data.remote.model.communication.SendMessageInput
import turmaA.grupoB.LinkStage.data.repository.CommunicationRepositoryInterface

class CommunicationViewModel(private val messageRepository: CommunicationRepositoryInterface) : ViewModel() {
    private val _uiState = MutableStateFlow<CommunicationUiState>(CommunicationUiState.Idle)
    val uiState: StateFlow<CommunicationUiState> = _uiState.asStateFlow()
    
    fun getNotificationsByUser(userId: String){
        viewModelScope.launch { 
            _uiState.value = CommunicationUiState.Loading
            try {
                val notifications = messageRepository.getNotificationsByUser(userId)

                _uiState.value = if (notifications.isEmpty()) {
                    CommunicationUiState.Empty
                } else {
                    CommunicationUiState.SuccessNotificationList(notifications)
                }
            } catch (e: Exception) {
                _uiState.value = CommunicationUiState.Error(
                    e.message ?: "Erro ao carregar notificações."
                )
            }
        }
    }
    fun getUnreadNotificationsByUser(userId: String){
        viewModelScope.launch {
            _uiState.value = CommunicationUiState.Loading
            try {
                val unreadNotifications = messageRepository.getUnreadNotificationsByUser(userId)

                _uiState.value = if (unreadNotifications.isEmpty()) {
                    CommunicationUiState.Empty
                } else {
                    CommunicationUiState.SuccessNotificationList(unreadNotifications)
                }
            } catch (e: Exception) {
                _uiState.value = CommunicationUiState.Error(
                    e.message ?: "Erro ao carregar notificações."
                )
            }
        }
    }
    fun markNotificationAsRead(notificationId: String){
        viewModelScope.launch {
            _uiState.value = CommunicationUiState.Loading
            try {
                val unreadNotification = messageRepository.markNotificationAsRead(notificationId)

                _uiState.value = if (unreadNotification  == null) {
                    CommunicationUiState.Empty
                } else {
                    CommunicationUiState.SuccessNotification(unreadNotification)
                }
            } catch (e: Exception) {
                _uiState.value = CommunicationUiState.Error(
                    e.message ?: "Erro ao marcar notificação como lida."
                )
            }
        }
    }
    fun getThreadById(threadId: String){
        viewModelScope.launch {
            _uiState.value = CommunicationUiState.Loading
            try {
                val thread = messageRepository.getThreadById(threadId)

                _uiState.value = if (thread == null) {
                    CommunicationUiState.Empty
                } else {
                    CommunicationUiState.SuccessThread(thread)
                }
            } catch (e: Exception) {
                _uiState.value = CommunicationUiState.Error(
                    e.message ?: "Erro ao carregar thread."
                )
            }
        }
    }
    fun getThreadsByInternship(internshipId: String){
        viewModelScope.launch {
            _uiState.value = CommunicationUiState.Loading
            try {
                val thread = messageRepository.getThreadsByInternship(internshipId)

                _uiState.value = if (thread.isEmpty()) {
                    CommunicationUiState.Empty
                } else {
                    CommunicationUiState.SuccessThreadList(thread)
                }
            } catch (e: Exception) {
                _uiState.value = CommunicationUiState.Error(
                    e.message ?: "Erro ao carregar threads."
                )
            }
        }
    }
    fun getThreadByApplication(applicationId: String){
        viewModelScope.launch {
            _uiState.value = CommunicationUiState.Loading
            try {
                val thread = messageRepository.getThreadByApplication(applicationId)

                _uiState.value = if (thread.isEmpty()) {
                    CommunicationUiState.Empty
                } else {
                    CommunicationUiState.SuccessThreadList(thread)
                }
            } catch (e: Exception) {
                _uiState.value = CommunicationUiState.Error(
                    e.message ?: "Erro ao carregar threads."
                )
            }
        }
    }
    fun getMessagesByThread(threadId: String){
        viewModelScope.launch {
            _uiState.value = CommunicationUiState.Loading
            try {
                val messages = messageRepository.getMessagesByThread(threadId)

                _uiState.value = if (messages.isEmpty()) {
                    CommunicationUiState.Empty
                } else {
                    CommunicationUiState.SuccessList(messages)
                }
            } catch (e: Exception) {
                _uiState.value = CommunicationUiState.Error(
                    e.message ?: "Erro ao carregar mensagens."
                )
            }
        }
    }
    fun sendMessage(input: SendMessageInput){
        viewModelScope.launch {
            _uiState.value = CommunicationUiState.Loading
            try {
                val message = messageRepository.sendMessage(input)

                _uiState.value = if (message == null) {
                    CommunicationUiState.Empty
                } else {
                    CommunicationUiState.Success(message)
                }
            } catch (e: Exception) {
                _uiState.value = CommunicationUiState.Error(
                    e.message ?: "Erro ao enviar mensagem."
                )
            }
        }
    }
    fun markMessageAsRead(messageId: String){
        viewModelScope.launch {
            _uiState.value = CommunicationUiState.Loading
            try {
                val message = messageRepository.markMessageAsRead(messageId)

                _uiState.value = if (message == null) {
                    CommunicationUiState.Empty
                } else {
                    CommunicationUiState.Success(message)
                }
            } catch (e: Exception) {
                _uiState.value = CommunicationUiState.Error(
                    e.message ?: "Erro ao marcar mensagem como lida."
                )
            }
        }
    }
}