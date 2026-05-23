package turmaA.grupoB.LinkStage.data.remote.model.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ApplicationStatus {
    @SerialName("PENDING")
    PENDING,

    @SerialName("ACCEPTED")
    ACCEPTED,

    @SerialName("REJECTED")
    REJECTED
}