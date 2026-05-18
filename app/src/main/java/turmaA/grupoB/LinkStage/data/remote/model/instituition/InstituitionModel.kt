package turmaA.grupoB.LinkStage.data.remote.model.instituition

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InstituitionModel(
    val id: String,

    @SerialName("user_id")
    val userId: String,

    val name: String,
    val address: String? = null,
    val website: String? = null,
    val description: String? = null,
    val sector: String? = null,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String
)
