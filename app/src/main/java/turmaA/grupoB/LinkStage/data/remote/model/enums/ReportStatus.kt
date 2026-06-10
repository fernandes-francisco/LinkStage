package turmaA.grupoB.LinkStage.data.remote.model.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ReportStatus {
    @SerialName("DRAFT")
    DRAFT,

    @SerialName("SUBMITTED")
    SUBMITTED,

    @SerialName("REVIEWED")
    REVIEWED
}