package turmaA.grupoB.LinkStage.data.remote.model.instituition

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OfferSkillModel(
    val id: String,

    @SerialName("offer_id")
    val offerId: String,

    val skill: String,

    @SerialName("created_at")
    val createdAt: String
)
