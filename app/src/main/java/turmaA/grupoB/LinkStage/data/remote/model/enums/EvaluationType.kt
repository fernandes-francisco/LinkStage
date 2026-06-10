package turmaA.grupoB.LinkStage.data.remote.model.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EvaluationType {
    @SerialName("INSTITUTION")
    INSTITUITION,

    @SerialName("SUPERVISOR")
    SUPERVISOR
}