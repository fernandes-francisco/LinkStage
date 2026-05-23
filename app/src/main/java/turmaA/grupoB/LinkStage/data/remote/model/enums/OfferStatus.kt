package turmaA.grupoB.LinkStage.data.remote.model.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class OfferStatus {
    @SerialName("DRAFT")
    DRAFT,

    @SerialName("PUBLISHED")
    PUBLISHED,

    @SerialName("CLOSED")
    CLOSED,

    @SerialName("REMOVED")
    REMOVED
}