package turmaA.grupoB.LinkStage.viewmodel.evaluation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import turmaA.grupoB.LinkStage.data.repository.EvaluationRepositoryInterface

class EvaluationViewModelFactory(
    private val evaluationRepository: EvaluationRepositoryInterface
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EvaluationViewModel(evaluationRepository) as T
    }
}