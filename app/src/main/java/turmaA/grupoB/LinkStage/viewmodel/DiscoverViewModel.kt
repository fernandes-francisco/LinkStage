package turmaA.grupoB.LinkStage.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import turmaA.grupoB.LinkStage.ui.aluno.offers.DiscoverFilters
import turmaA.grupoB.LinkStage.ui.aluno.offers.OfferItem
import androidx.compose.ui.graphics.Color

class DiscoverViewModel : ViewModel() {

    val availableAreas = listOf(
        "Tecnologia", "Design", "Marketing", "Engenharia",
        "Saúde", "Educação", "Finanças", "Hotelaria",
    )

    private val _filters = MutableStateFlow(DiscoverFilters())
    val filters: StateFlow<DiscoverFilters> = _filters.asStateFlow()

    private val _allOffers = MutableStateFlow(sampleOffers)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filteredOffers = MutableStateFlow(sampleOffers)
    val filteredOffers: StateFlow<List<OfferItem>> = _filteredOffers.asStateFlow()

    val hasActiveFilters: Boolean
        get() = _filters.value != DiscoverFilters()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun applyFilters(newFilters: DiscoverFilters = _filters.value) {
        _filters.value = newFilters
        val query = _searchQuery.value
        val filters = _filters.value

        _filteredOffers.value = _allOffers.value.filter { offer ->
            val matchesSearch = query.isEmpty() ||
                offer.title.contains(query, ignoreCase = true) ||
                offer.company.contains(query, ignoreCase = true)

            val matchesArea = filters.areas.isEmpty() ||
                filters.areas.any { offer.area.contains(it, ignoreCase = true) }

            val matchesLocation = filters.location.isEmpty() ||
                offer.location.contains(filters.location, ignoreCase = true)

            val matchesWorkModel = filters.workModel == null ||
                offer.type.contains(filters.workModel, ignoreCase = true)

            val matchesDuration = filters.duration == null ||
                offer.duration.contains(filters.duration, ignoreCase = true)

            matchesSearch && matchesArea && matchesLocation && matchesWorkModel && matchesDuration
        }
    }

    fun clearFilters() {
        _filters.value = DiscoverFilters()
        applyFilters()
    }
}

private val sampleOffers = listOf(
    OfferItem("1", "Designer de Produto", "Continente – Tempo Inteiro", "Tempo Inteiro", "5h atrás", Color(0xFFE53935), "C", duration = "6 meses", area = "Design", location = "Porto"),
    OfferItem("2", "UI/UX Designer", "Viana S.T.Arts – Remoto", "Remoto", "2d atrás", Color(0xFF212121), "V", isFavourite = true, duration = "3 meses", area = "Design", location = "Remoto"),
    OfferItem("3", "Aprendiz de cozinha", "McDonald's – Tempo parcial", "Tempo Parcial", "5d atrás", Color(0xFFFFCC00), "M", duration = "12 meses", area = "Hotelaria", location = "Viana do Castelo"),
    OfferItem("4", "Recepcionista", "B&B – Tempo Inteiro", "Tempo Inteiro", "1w atrás", Color(0xFF1565C0), "B", duration = "6 meses", area = "Hotelaria", location = "Braga"),
    OfferItem("5", "Programador Full-Stack", "IPVC Tech – Remoto", "Remoto", "2w atrás", Color(0xFF37474F), "I", duration = "9 meses", area = "Tecnologia", location = "Remoto"),
)