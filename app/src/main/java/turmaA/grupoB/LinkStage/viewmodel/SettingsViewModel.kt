package turmaA.grupoB.LinkStage.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import turmaA.grupoB.LinkStage.ui.aluno.settings.LoggedUser

class SettingsViewModel : ViewModel() {

    private val _currentLanguage = MutableStateFlow("PT")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _notifCandidaturas = MutableStateFlow(true)
    val notifCandidaturas: StateFlow<Boolean> = _notifCandidaturas.asStateFlow()

    private val _notifMensagens = MutableStateFlow(true)
    val notifMensagens: StateFlow<Boolean> = _notifMensagens.asStateFlow()

    private val _notifLembretes = MutableStateFlow(false)
    val notifLembretes: StateFlow<Boolean> = _notifLembretes.asStateFlow()

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

    fun toggleNotifications(enabled: Boolean) {
        _notificationsEnabled.value = enabled
    }

    fun toggleNotifCandidaturas(enabled: Boolean) {
        _notifCandidaturas.value = enabled
    }

    fun toggleNotifMensagens(enabled: Boolean) {
        _notifMensagens.value = enabled
    }

    fun toggleNotifLembretes(enabled: Boolean) {
        _notifLembretes.value = enabled
    }

    fun logout() {
        // Supabase auth sign out will be implemented here
    }
}