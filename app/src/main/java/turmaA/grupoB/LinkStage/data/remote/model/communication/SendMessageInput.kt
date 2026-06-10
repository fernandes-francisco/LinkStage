package turmaA.grupoB.LinkStage.data.remote.model.communication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageInput(
    @SerialName("thread_id")
    val threadId: String,

    @SerialName("sender_id")
    val senderId: String,

    val content: String
)
