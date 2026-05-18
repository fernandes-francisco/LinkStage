package turmaA.grupoB.LinkStage.data.remote.model.communication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageModel(
    val id: String,

    @SerialName("thread_id")
    val threadId: String,

    @SerialName("sender_id")
    val senderId: String,

    val content: String,

    @SerialName("is_read")
    val isRead: Boolean = false,

    @SerialName("created_at")
    val createdAt: String
)
