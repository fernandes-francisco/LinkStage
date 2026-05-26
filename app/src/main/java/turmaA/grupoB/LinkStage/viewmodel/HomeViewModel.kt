package turmaA.grupoB.LinkStage.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import turmaA.grupoB.LinkStage.ui.aluno.activity.ActiveInternship
import turmaA.grupoB.LinkStage.ui.aluno.activity.ActivityLog
import turmaA.grupoB.LinkStage.ui.aluno.activity.ActivityLogStatus
import turmaA.grupoB.LinkStage.ui.aluno.activity.ApplicationItem
import turmaA.grupoB.LinkStage.ui.aluno.chat.Conversation
import turmaA.grupoB.LinkStage.ui.aluno.chat.sampleConversations
import turmaA.grupoB.LinkStage.ui.aluno.home.ApplicationStatus
import java.time.LocalDate

class HomeViewModel : ViewModel() {

    private val _hasActiveInternship = MutableStateFlow(false)
    val hasActiveInternship: StateFlow<Boolean> = _hasActiveInternship.asStateFlow()

    private val _activeInternship = MutableStateFlow<ActiveInternship?>(null)
    val activeInternship: StateFlow<ActiveInternship?> = _activeInternship.asStateFlow()

    private val _recentApplications = MutableStateFlow(sampleRecentApplications)
    val recentApplications: StateFlow<List<ApplicationItem>> = _recentApplications.asStateFlow()

    private val _activeApplications = MutableStateFlow(sampleActiveApplications)
    val activeApplications: StateFlow<List<ApplicationItem>> = _activeApplications.asStateFlow()

    private val _pastApplications = MutableStateFlow(samplePastApplications)
    val pastApplications: StateFlow<List<ApplicationItem>> = _pastApplications.asStateFlow()

    private val _recentConversations = MutableStateFlow(sampleConversations.take(3))
    val recentConversations: StateFlow<List<Conversation>> = _recentConversations.asStateFlow()

    fun setActiveInternship(internship: ActiveInternship?) {
        _activeInternship.value = internship
        _hasActiveInternship.value = internship != null
    }

    fun toggleInternship() {
        if (_hasActiveInternship.value) {
            setActiveInternship(null)
        } else {
            setActiveInternship(mockActiveInternship)
        }
    }
}

private val mockActiveInternship = ActiveInternship(
    title = "Estágio em Desenvolvimento Web",
    startDate = LocalDate.now().minusMonths(2),
    endDate = LocalDate.now().plusMonths(4),
    activityLogs = listOf(
        ActivityLog("1", "Relatório semanal #1", "Entrega do primeiro relatório", LocalDate.now().minusWeeks(3), ActivityLogStatus.COMPLETED),
        ActivityLog("2", "Ponto de controlo 1", "Apresentação de progresso", LocalDate.now().minusWeeks(1), ActivityLogStatus.COMPLETED),
        ActivityLog("3", "Relatório semanal #2", "Entrega do segundo relatório", LocalDate.now().plusWeeks(1), ActivityLogStatus.PENDING),
    ),
)

private val sampleRecentApplications = listOf(
    ApplicationItem("1", "Call Center Indiano", "Rasheed", "3d atrás", ApplicationStatus.PENDING),
    ApplicationItem("2", "Intermaché dos Arcos", "Intermarché", "2w atrás", ApplicationStatus.ACCEPTED),
    ApplicationItem("3", "UI/UX Designer", "Viana S.T.Arts", "1w atrás", ApplicationStatus.REJECTED),
)

private val sampleActiveApplications = listOf(
    ApplicationItem("1", "Call Center Indiano", "Rasheed", "3d atrás", ApplicationStatus.PENDING),
    ApplicationItem("2", "Intermaché dos Arcos", "Intermarché", "2w atrás", ApplicationStatus.PENDING),
)

private val samplePastApplications = listOf(
    ApplicationItem("3", "Call Center Indiano", "Rasheed", "3d atrás", ApplicationStatus.REJECTED),
)