package turmaA.grupoB.LinkStage.data.remote.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SupervisorSkillModel(
    val id: String,

    @SerialName("supervisor_id")
    val supervisorId: String,

    val skill: String,

    @SerialName("created_at")
    val createdAt: String
)
