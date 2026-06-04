package turmaA.grupoB.LinkStage.viewmodel.evaluation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import turmaA.grupoB.LinkStage.data.remote.model.enums.EvaluationType
import turmaA.grupoB.LinkStage.data.remote.model.evaluation.CreateEvaluationInput
import turmaA.grupoB.LinkStage.data.repository.EvaluationRepositoryInterface

class EvaluationViewModel(
    private val evaluationRepository: EvaluationRepositoryInterface
) : ViewModel() {

    private val _uiState = MutableStateFlow<EvaluationUiState>(EvaluationUiState.Idle)
    val uiState: StateFlow<EvaluationUiState> = _uiState.asStateFlow()

    fun loadEvaluations() {
        viewModelScope.launch {
            _uiState.value = EvaluationUiState.Loading

            try {
                val evaluations = evaluationRepository.getEvaluations()

                _uiState.value = if (evaluations.isEmpty()) {
                    EvaluationUiState.Empty
                } else {
                    EvaluationUiState.SuccessList(evaluations)
                }

            } catch (e: Exception) {
                _uiState.value = EvaluationUiState.Error(
                    e.message ?: "Erro ao carregar avaliações."
                )
            }
        }
    }

    fun loadEvaluationsByInternship(internshipId: String) {
        viewModelScope.launch {
            _uiState.value = EvaluationUiState.Loading

            try {
                val evaluations = evaluationRepository.getEvaluationsByInternship(internshipId)

                _uiState.value = if (evaluations.isEmpty()) {
                    EvaluationUiState.Empty
                } else {
                    EvaluationUiState.SuccessList(evaluations)
                }
            } catch (e: Exception) {
                _uiState.value = EvaluationUiState.Error(
                    e.message ?: "Erro ao carregar avaliações do estágio"
                )
            }
        }
    }

    fun loadEvaluationByInternshipAndType(
        internshipId: String,
        evaluationType: EvaluationType
    ) {
        viewModelScope.launch {
            _uiState.value = EvaluationUiState.Loading

            try {
                val evaluation = evaluationRepository.getEvaluationByInternshipAndType(
                    internshipId,
                    evaluationType
                )

                _uiState.value = if (evaluation != null) {
                    EvaluationUiState.Success(evaluation)
                } else {
                    EvaluationUiState.Empty
                }
            } catch (e: Exception) {
                _uiState.value = EvaluationUiState.Error(
                    e.message ?: "Erro ao carregar avaliação por tipo."
                )
            }
        }
    }

    fun createEvaluation(input: CreateEvaluationInput) {
        viewModelScope.launch {
            _uiState.value = EvaluationUiState.Loading

            try {
                val evaluation = evaluationRepository.createEvaluation(input)
                _uiState.value = EvaluationUiState.Success(evaluation)
            } catch (e: Exception) {
                _uiState.value = EvaluationUiState.Error(
                    e.message ?: "Erro ao criar avaliação."
                )
            }
        }
    }

    fun loadGradingByInternship(internshipId: String) {
        viewModelScope.launch {
            _uiState.value = EvaluationUiState.Loading

            try {
                val grading = evaluationRepository.getFinalGradeByInternship(internshipId)

                _uiState.value = if(grading != null) {
                    EvaluationUiState.FinalGradeSuccess(grading)
                } else {
                    EvaluationUiState.Empty
                }
            } catch (e: Exception) {
                _uiState.value = EvaluationUiState.Error(
                    e.message ?: "Erro ao carregar nota final."
                )
            }
        }
    }

    fun resetState(){
        _uiState.value = EvaluationUiState.Idle
    }

}