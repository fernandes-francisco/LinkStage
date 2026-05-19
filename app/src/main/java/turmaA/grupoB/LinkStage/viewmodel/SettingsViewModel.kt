package turmaA.grupoB.LinkStage.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import turmaA.grupoB.LinkStage.ui.aluno.settings.LoggedUser

class SettingsViewModel : ViewModel() {

    private val _currentLanguage = MutableStateFlow("PT")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private val _user = MutableStateFlow(
        LoggedUser(
            name = "Tomás Silva",
            email = "tomas.silva@ipvc.pt",
        )
    )
    val user: StateFlow<LoggedUser> = _user.asStateFlow()

    fun changeLanguage(lang: String) {
        _currentLanguage.value = lang
    }

    fun logout() {
        // Supabase auth sign out will be implemented here
    }
}