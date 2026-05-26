package turmaA.grupoB.LinkStage.viewmodel.offer

import turmaA.grupoB.LinkStage.data.remote.model.offer.InternshipOfferModel

sealed class OfferUiState {
    data object Idle : OfferUiState()
    data object Loading : OfferUiState()
    data class Success(val offer: InternshipOfferModel) : OfferUiState()
    data class SuccessList(val offers: List<InternshipOfferModel>) : OfferUiState()
    data object Empty : OfferUiState()
    data class Error(val message: String) : OfferUiState()
}