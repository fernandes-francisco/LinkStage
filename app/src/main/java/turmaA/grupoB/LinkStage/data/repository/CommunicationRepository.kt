package turmaA.grupoB.LinkStage.data.repository

import io.github.jan.supabase.postgrest.from
import turmaA.grupoB.LinkStage.data.remote.model.communication.MessageModel
import turmaA.grupoB.LinkStage.data.remote.model.communication.MessageThreadModel
import turmaA.grupoB.LinkStage.data.remote.model.communication.MessageThreadParticipantModel
import turmaA.grupoB.LinkStage.data.remote.model.communication.NotificationModel
import turmaA.grupoB.LinkStage.data.remote.model.communication.SendMessageInput
import turmaA.grupoB.LinkStage.data.remote.supabase.SupabaseClientProvider

class CommunicationRepository : CommunicationRepositoryInterface {

    private val supabase = SupabaseClientProvider.client

    override suspend fun getNotificationsByUser(userId: String): List<NotificationModel> {
        return supabase
            .from("notifications")
            .select {
                filter {
                    eq("user_id", userId)
                }
            }
            .decodeList<NotificationModel>()
    }

    override suspend fun getUnreadNotificationsByUser(userId: String): List<NotificationModel> {
        return supabase
            .from("notifications")
            .select {
                filter {
                    eq("user_id", userId)
                    eq("is_read", false)
                }
            }
            .decodeList<NotificationModel>()
    }

    override suspend fun markNotificationAsRead(notificationId: String): NotificationModel? {
        val updateData = mapOf(
            "is_read" to true
        )

        return try {
            supabase
                .from("notifications")
                .update(updateData) {
                    select()
                    filter {
                        eq("id", notificationId)
                    }
                }
                .decodeSingle<NotificationModel>()
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun getThreadById(threadId: String): MessageThreadModel? {
        return supabase
            .from("message_threads")
            .select {
                filter {
                    eq("id", threadId)
                }
            }
            .decodeList<MessageThreadModel>()
            .firstOrNull()
    }

    override suspend fun getThreadsByInternship(internshipId: String): List<MessageThreadModel> {
        return supabase
            .from("message_threads")
            .select {
                filter {
                    eq("internship_id", internshipId)
                }
            }
            .decodeList<MessageThreadModel>()
    }

    override suspend fun getThreadByApplication(applicationId: String): List<MessageThreadModel> {
        return supabase
            .from("message_threads")
            .select {
                filter {
                    eq("application_id", applicationId)
                }
            }
            .decodeList<MessageThreadModel>()
    }

    override suspend fun getParticipantsByThread(threadId: String): List<MessageThreadParticipantModel> {
        return supabase
            .from("message_thread_participants")
            .select {
                filter {
                    eq("thread_id", threadId)
                }
            }
            .decodeList<MessageThreadParticipantModel>()
    }

    override suspend fun getMessagesByThread(threadId: String): List<MessageModel> {
        return supabase
            .from("messages")
            .select {
                filter {
                    eq("thread_id", threadId)
                }
            }
            .decodeList<MessageModel>()
    }

    override suspend fun sendMessage(input: SendMessageInput): MessageModel? {
        return try {
            supabase
                .from("messages")
                .insert(input) {
                    select()
                }
                .decodeSingle<MessageModel>()
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun markMessageAsRead(messageId: String): MessageModel? {
        val updateData = mapOf(
            "is_read" to true
        )

        return try {
            supabase
                .from("messages")
                .update(updateData) {
                    select()
                    filter {

                        eq("id", messageId)
                    }
                }
                .decodeSingle<MessageModel>()
        } catch (_: Exception) {
            null
        }
    }
}