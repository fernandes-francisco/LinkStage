package turmaA.grupoB.LinkStage.data.remote.model.evaluation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import turmaA.grupoB.LinkStage.data.remote.model.enums.EvaluationType

@Serializable
data class CreateEvaluationInput(
    @SerialName("internship_id")
    val internshipId: String,

    @SerialName("evaluator_user_id")
    val evaluatorUserId: String,

    @SerialName("evaluator_type")
    val evaluatorType: EvaluationType,

    val grade: Double,
    val comment: String? = null
)
