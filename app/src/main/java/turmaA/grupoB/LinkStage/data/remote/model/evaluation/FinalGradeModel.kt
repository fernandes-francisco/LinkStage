package turmaA.grupoB.LinkStage.data.remote.model.evaluation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FinalGradeModel(
    val id: String,

    @SerialName("internship_id")
    val internshipId: String,

    val grade: Double,
    val comment: String? = null,

    @SerialName("created_at")
    val createdAt: String
)
