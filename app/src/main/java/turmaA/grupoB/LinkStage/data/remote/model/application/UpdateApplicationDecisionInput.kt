package turmaA.grupoB.LinkStage.data.remote.model.application

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import turmaA.grupoB.LinkStage.data.remote.model.enums.ApplicationStatus

@Serializable
data class UpdateApplicationDecisionInput(
    val status: ApplicationStatus,

    @SerialName("rejection_reason")
    val rejectionReason: String? = null,

    @SerialName("decision_at")
    val decisionAt: String
)
