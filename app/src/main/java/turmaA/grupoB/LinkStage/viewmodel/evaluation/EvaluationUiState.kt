package turmaA.grupoB.LinkStage.viewmodel.evaluation

import turmaA.grupoB.LinkStage.data.remote.model.evaluation.EvaluationModel
import turmaA.grupoB.LinkStage.data.remote.model.evaluation.FinalGradeModel

sealed class EvaluationUiState {
    data object Idle: EvaluationUiState()
    data object Loading: EvaluationUiState()
    data class Success(val evaluation: EvaluationModel): EvaluationUiState()
    data class SuccessList(val evaluations: List<EvaluationModel>) : EvaluationUiState()
    data class FinalGradeSuccess(val finalGrade: FinalGradeModel) : EvaluationUiState()
    data object Empty: EvaluationUiState()
    data class Error(val message: String) : EvaluationUiState()
}