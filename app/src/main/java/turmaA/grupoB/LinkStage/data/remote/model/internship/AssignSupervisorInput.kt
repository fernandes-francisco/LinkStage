package turmaA.grupoB.LinkStage.data.remote.model.internship

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import turmaA.grupoB.LinkStage.data.remote.model.enums.InternshipStatus

@Serializable
data class AssignSupervisorInput(
    @SerialName("supervisor_id")
    val supervisorId: String,

    val status: InternshipStatus = InternshipStatus.IN_PROGRESS
)
