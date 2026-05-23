package turmaA.grupoB.LinkStage.data.remote.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SupervisorModel(
    val id: String,

    @SerialName("user_id")
    val userId: String,

    val department: String? = null,
    val specialty: String? = null,

    @SerialName("max_internships")
    val maxInternships: Int = 5,

    @SerialName("accepts_new_internships")
    val acceptsNewInternships: Boolean = true,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String
)
