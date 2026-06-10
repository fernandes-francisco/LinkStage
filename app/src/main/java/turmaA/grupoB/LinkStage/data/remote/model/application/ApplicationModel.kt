package turmaA.grupoB.LinkStage.data.remote.model.application

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import turmaA.grupoB.LinkStage.data.remote.model.enums.ApplicationStatus

@Serializable
data class ApplicationModel(
    val id: String,

    @SerialName("offer_id")
    val offerId: String,

    @SerialName("student_id")
    val studentId: String,

    val status: ApplicationStatus = ApplicationStatus.PENDING,

    @SerialName("motivation_letter")
    val motivationLetter: String? = null,

    @SerialName("rejection_reason")
    val rejectionReason: String? = null,

    @SerialName("decision_at")
    val decisionAt: String? = null,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt : String
)
