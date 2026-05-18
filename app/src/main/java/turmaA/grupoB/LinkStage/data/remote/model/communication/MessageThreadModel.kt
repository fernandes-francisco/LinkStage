package turmaA.grupoB.LinkStage.data.remote.model.communication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageThreadModel(
    val id: String,

    @SerialName("internship_id")
    val internshipId: String? = null,

    @SerialName("application_id")
    val applicationId: String? = null,

    @SerialName("created_at")
    val createdAt: String
)
