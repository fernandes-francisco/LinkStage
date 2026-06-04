package turmaA.grupoB.LinkStage.viewmodel.communication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import turmaA.grupoB.LinkStage.data.repository.CommunicationRepositoryInterface

class CommunicationViewModelFactory(
    private val communicationRepository: CommunicationRepositoryInterface
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommunicationViewModel(communicationRepository) as T
    }
}