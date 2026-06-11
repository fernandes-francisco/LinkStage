package turmaA.grupoB.LinkStage.viewmodel.flags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import turmaA.grupoB.LinkStage.data.repository.flags.FlagsRepositoryInterface
import turmaA.grupoB.LinkStage.util.DebugLogger

private const val TAG = "FlagsViewModel"

class FlagsViewModel (
    private val flagsRepository: FlagsRepositoryInterface
): ViewModel(){
    private val _uiState = MutableStateFlow<FlagsUIState>(FlagsUIState.Idle)
    val uiState: StateFlow<FlagsUIState> = _uiState.asStateFlow()

    fun getImages(names: List<String>){
        DebugLogger.d(TAG, "getImages called with names=$names")
        viewModelScope.launch {
            _uiState.value = FlagsUIState.Loading
            DebugLogger.d(TAG, "uiState=Loading")
            try {
                val results = names.map { name ->
                    DebugLogger.d(TAG, "fetching flag for country=$name")
                    flagsRepository.getFlag(name).also { img ->
                        DebugLogger.d(TAG, "fetched country=$name img=${img?.png}")
                    }
                }
                _uiState.value = if (results.isEmpty()) {
                    DebugLogger.d(TAG, "uiState=Empty")
                    FlagsUIState.Empty
                } else {
                    DebugLogger.d(TAG, "uiState=Success results=${results.map { it?.png }}")
                    FlagsUIState.Success(results)
                }
            }catch (e: Exception){
                DebugLogger.e(TAG, "error loading flags for names=$names", e)
                _uiState.value = FlagsUIState.Error(
                    e.message ?: "Erro ao carregar imagens"
                )
            }
        }
    }
}