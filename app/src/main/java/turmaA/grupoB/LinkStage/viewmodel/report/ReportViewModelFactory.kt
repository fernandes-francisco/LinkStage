package turmaA.grupoB.LinkStage.viewmodel.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import turmaA.grupoB.LinkStage.data.repository.report.ReportRepositoryInterface

class ReportViewModelFactory(
    private val reportRepository: ReportRepositoryInterface
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReportViewModel(reportRepository) as T
    }

}