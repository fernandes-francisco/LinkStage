package turmaA.grupoB.LinkStage.viewmodel.offer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import turmaA.grupoB.LinkStage.data.repository.OfferRepository

class OfferViewModelFactory(
    private val offerRepository: OfferRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return OfferViewModel(offerRepository) as T
    }
}