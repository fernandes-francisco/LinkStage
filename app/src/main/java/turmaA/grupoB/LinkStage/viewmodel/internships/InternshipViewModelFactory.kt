package turmaA.grupoB.LinkStage.viewmodel.internships

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import turmaA.grupoB.LinkStage.data.repository.InternshipRepositoryInterface

class InternshipViewModelFactory(
    private val internshipRepository: InternshipRepositoryInterface
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return InternshipViewModel(internshipRepository) as T
    }
}