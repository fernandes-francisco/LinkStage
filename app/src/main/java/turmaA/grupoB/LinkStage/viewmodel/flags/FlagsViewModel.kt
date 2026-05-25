package turmaA.grupoB.LinkStage.viewmodel.flags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import turmaA.grupoB.LinkStage.data.repository.Flagsrepository

class FlagsViewModel (
    private val flagsrepository: Flagsrepository
): ViewModel(){
    private val _uiState = MutableStateFlow<FlagsUIState>(FlagsUIState.Idle)
    val uiState: StateFlow<FlagsUIState> = _uiState.asStateFlow()

    fun getImages(names: List<String>){
        viewModelScope.launch {
            _uiState.value = FlagsUIState.Loading
            try {
                for (name in names){
                    val image = flagsrepository.getFlag(name)
                    _uiState.value = if (image != null){
                        FlagsUIState.Success(image)
                    }else{
                        FlagsUIState.Empty
                    }
                }
            }catch (e: Exception){
                _uiState.value = FlagsUIState.Error(
                    e.message ?: "Erro ao carregar imagens"
                )
            }
        }
    }
}