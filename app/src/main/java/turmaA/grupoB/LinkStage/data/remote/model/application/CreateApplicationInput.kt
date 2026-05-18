package turmaA.grupoB.LinkStage.data.remote.model.application

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import turmaA.grupoB.LinkStage.data.remote.model.enums.ApplicationStatus

@Serializable
data class CreateApplicationInput(
    @SerialName("offer_id")
    val offerId: String,

    @SerialName("student_id")
    val studentId: String,

    val status: ApplicationStatus = ApplicationStatus.PENDING,

    @SerialName("motivation_letter")
    val motivationLetter: String? = null
)
