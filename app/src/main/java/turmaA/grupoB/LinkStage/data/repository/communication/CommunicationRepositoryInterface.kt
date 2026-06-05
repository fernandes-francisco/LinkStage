package turmaA.grupoB.LinkStage.data.repository.communication

import turmaA.grupoB.LinkStage.data.remote.model.communication.MessageModel
import turmaA.grupoB.LinkStage.data.remote.model.communication.MessageThreadModel
import turmaA.grupoB.LinkStage.data.remote.model.communication.MessageThreadParticipantModel
import turmaA.grupoB.LinkStage.data.remote.model.communication.NotificationModel
import turmaA.grupoB.LinkStage.data.remote.model.communication.SendMessageInput

interface CommunicationRepositoryInterface {
    suspend fun getNotificationsByUser(userId: String): List<NotificationModel>
    suspend fun getUnreadNotificationsByUser(userId: String): List<NotificationModel>
    suspend fun markNotificationAsRead(notificationId: String): NotificationModel?
    suspend fun getThreadById(threadId: String): MessageThreadModel?
    suspend fun getThreadsByInternship(internshipId: String): List<MessageThreadModel>
    suspend fun getThreadByApplication(applicationId: String): List<MessageThreadModel>
    suspend fun getParticipantsByThread(threadId: String): List<MessageThreadParticipantModel>
    suspend fun getMessagesByThread(threadId: String): List<MessageModel>
    suspend fun sendMessage(input: SendMessageInput): MessageModel?
    suspend fun markMessageAsRead(messageId: String): MessageModel?
}