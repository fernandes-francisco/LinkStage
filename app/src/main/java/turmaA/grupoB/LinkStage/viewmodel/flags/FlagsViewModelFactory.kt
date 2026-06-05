package turmaA.grupoB.LinkStage.viewmodel.flags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import turmaA.grupoB.LinkStage.data.repository.flags.FlagsRepository

class FlagsViewModelFactory(
    private val flagsRepository: FlagsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass : Class<T>): T {
        return FlagsViewModel(flagsRepository)as T
    }
}