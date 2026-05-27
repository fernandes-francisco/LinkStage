package turmaA.grupoB.LinkStage.viewmodel.offer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import turmaA.grupoB.LinkStage.data.remote.model.offer.CreateOfferInput
import turmaA.grupoB.LinkStage.data.remote.model.offer.UpdateOfferInput
import turmaA.grupoB.LinkStage.data.repository.OfferRepositoryInterface

class OfferViewModel(
    private val offerRepository: OfferRepositoryInterface
) : ViewModel() {

    private val _uiState = MutableStateFlow<OfferUiState>(OfferUiState.Idle)
    val uiState: StateFlow<OfferUiState> = _uiState.asStateFlow()

    fun loadPublishedOffers() {
        viewModelScope.launch {
            _uiState.value = OfferUiState.Loading

            try {
                val offers = offerRepository.getPublishedOffers()

                _uiState.value = if (offers.isEmpty()) {
                    OfferUiState.Empty
                } else {
                    OfferUiState.SuccessList(offers)
                }

            } catch (e: Exception) {
                _uiState.value = OfferUiState.Error(
                    e.message ?: "Erro ao carregar ofertas publicadas."
                )
            }
        }
    }

    fun loadOffersByInstitution(institutionId: String) {
        viewModelScope.launch {
            _uiState.value = OfferUiState.Loading

            try {
                val offers = offerRepository.getOffersByInstitution((institutionId))

                _uiState.value = if (offers.isEmpty()) {
                    OfferUiState.Empty
                } else {
                    OfferUiState.SuccessList(offers)
                }
            } catch (e: Exception) {
                _uiState.value = OfferUiState.Error(
                    e.message ?: "Erro ao carregar ofertas da instituição."
                )
            }
        }
    }

    fun loadOffersById(offerId: String) {
        viewModelScope.launch {
            _uiState.value = OfferUiState.Loading

            try {
                val offer = offerRepository.getOfferById(offerId)

                _uiState.value = if (offer != null) {
                    OfferUiState.Success(offer)
                } else {
                    OfferUiState.Empty
                }

            } catch (e: Exception) {
                _uiState.value = OfferUiState.Error(
                    e.message ?: "Erro ao carregar oferta."
                )
            }
        }
    }

    fun createOffer(input: CreateOfferInput) {
        viewModelScope.launch {
            _uiState.value = OfferUiState.Loading

            try {
                val offer = offerRepository.createOffer(input)
                _uiState.value = OfferUiState.Success(offer)
            } catch (e: Error) {
                _uiState.value = OfferUiState.Error(
                    e.message ?: "Erro ao criar oferta."
                )
            }
        }
    }

    fun updateOffer(offerId: String, input: UpdateOfferInput) {
        viewModelScope.launch {
            _uiState.value = OfferUiState.Loading

            try {
                val offer = offerRepository.updateOffer(offerId, input)

                _uiState.value = OfferUiState.Success(offer)
            } catch (e: Exception) {
                _uiState.value = OfferUiState.Error(
                    e.message ?: "Erro ao atualizar oferta."
                )
            }
        }
    }

    fun closeOffer(offerId: String) {
        viewModelScope.launch {
            _uiState.value = OfferUiState.Loading

            try {
                val offer = offerRepository.closeOffer(offerId)
                _uiState.value = OfferUiState.Success(offer)
            } catch (e: Exception) {
                _uiState.value = OfferUiState.Error(
                    e.message ?: "Erro ao fechar oferta."
                )
            }
        }
    }

    fun markOfferAsRemoved(offerId: String) {
        viewModelScope.launch {
            _uiState.value = OfferUiState.Loading

            try {
                val offer = offerRepository.markOfferAsRemoved(offerId)
                _uiState.value = OfferUiState.Success(offer)
            } catch (e: Exception) {
                _uiState.value = OfferUiState.Error(
                    e.message ?: "Erro ao remover oferta."
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = OfferUiState.Idle
    }
}