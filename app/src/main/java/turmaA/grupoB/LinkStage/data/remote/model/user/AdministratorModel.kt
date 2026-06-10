package turmaA.grupoB.LinkStage.data.remote.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdministratorModel(
    val id: String,

    @SerialName("user_id")
    val userdId: String,

    @SerialName("created_at")
    val createdAt: String
)
