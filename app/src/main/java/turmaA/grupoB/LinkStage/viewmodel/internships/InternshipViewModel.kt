package turmaA.grupoB.LinkStage.viewmodel.internships

import android.R.id.input
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.utils.EmptyContent.status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import turmaA.grupoB.LinkStage.data.remote.model.enums.InternshipStatus
import turmaA.grupoB.LinkStage.data.remote.model.internship.ActivityLogModel
import turmaA.grupoB.LinkStage.data.remote.model.internship.AssignSupervisorInput
import turmaA.grupoB.LinkStage.data.remote.model.internship.CreateActivityLogInput
import turmaA.grupoB.LinkStage.data.remote.model.internship.InternshipModel
import turmaA.grupoB.LinkStage.data.repository.InternshipRepositoryInterface

class InternshipViewModel(
    private val internshipRepository: InternshipRepositoryInterface
) : ViewModel (){
    private val _uiState = MutableStateFlow<InternshipUiState>(InternshipUiState.Idle)
    val uiState: StateFlow<InternshipUiState> = _uiState.asStateFlow()
    fun getInternships(){
        viewModelScope.launch {
            _uiState.value = InternshipUiState.Loading
            try {
                val internships = internshipRepository.getInterships()
                _uiState.value = if (internships.isEmpty()){
                    InternshipUiState.Empty
                }else{
                    InternshipUiState.SuccessList(internships)
                }
            }catch (e: Exception){
                _uiState.value = InternshipUiState.Error(
                    e.message ?: "Erro ao carregar estágios"
                )
            }
        }
    }
    fun getInternshipByStudent(studentId: String){
        viewModelScope.launch {
            _uiState.value = InternshipUiState.Loading
            try {
                val internships = internshipRepository.getInternshipsByStudent(studentId)
                _uiState.value = if (internships.isEmpty()){
                    InternshipUiState.Empty
                }else{
                    InternshipUiState.SuccessList(internships)
                }
            }catch (e: Exception){
                _uiState.value = InternshipUiState.Error(
                    e.message ?: "Erro ao carregar estágios"
                )
            }
        }
    }
    fun getInternshipsByInstitution(institutionId: String) {
        viewModelScope.launch {
            _uiState.value = InternshipUiState.Loading
            try {
                val internships = internshipRepository.getInternshipsByInstitution(institutionId)
                _uiState.value = if (internships.isEmpty()){
                    InternshipUiState.Empty
                }else{
                    InternshipUiState.SuccessList(internships)
                }
            }catch (e: Exception){
                _uiState.value = InternshipUiState.Error(
                    e.message ?: "Erro ao carregar estágios"
                )
            }
        }
    }
    fun getInternshipsByStatus(status: InternshipStatus){
        viewModelScope.launch {
            _uiState.value = InternshipUiState.Loading
            try {
                val internships = internshipRepository.getInternshipsByStatus(status)
                _uiState.value = if (internships.isEmpty()){
                    InternshipUiState.Empty
                }else{
                    InternshipUiState.SuccessList(internships)
                }
            }catch (e: Exception){
                _uiState.value = InternshipUiState.Error(
                    e.message ?: "Erro ao carregar estágios"
                )
            }
        }
    }
    fun getInternshipById(internshipId: String){
        viewModelScope.launch {
            _uiState.value = InternshipUiState.Loading
            try {
                val internship = internshipRepository.getInternshipById(internshipId)
                _uiState.value = if (internship != null){
                    InternshipUiState.Success(internship)
                }else{
                    InternshipUiState.Empty
                }
            }catch (e: Exception){
                _uiState.value = InternshipUiState.Error(
                    e.message ?: "Erro ao carregar estágios"
                )
            }
        }
    }
    fun assignSupervisor(internshipId: String, input: AssignSupervisorInput){
        viewModelScope.launch {
            _uiState.value = InternshipUiState.Loading
            try {
                val internship = internshipRepository.assingSupervisor(internshipId, input)
                _uiState.value = if (internship != null){
                    InternshipUiState.Success(internship)
                }else{
                    InternshipUiState.Empty
                }
            }catch (e: Exception){
                _uiState.value = InternshipUiState.Error(
                    e.message ?: "Erro ao carregar estágios"
                )
            }
        }
    }
    fun createActivityLog(input: CreateActivityLogInput ){
        viewModelScope.launch {
            _uiState.value = InternshipUiState.Loading
            try {
                val activity = internshipRepository.createActivityLog(input)
                _uiState.value = if (activity != null){
                    InternshipUiState.SuccessActivity(activity)
                }else{
                    InternshipUiState.Empty
                }
            }catch (e: Exception){
                _uiState.value = InternshipUiState.Error(
                    e.message ?: "Erro ao carregar estágios"
                )
            }
        }
    }
    fun getActivityLogsByInternship(internshipId : String){
        viewModelScope.launch {
            _uiState.value = InternshipUiState.Loading
            try {
                val activity = internshipRepository.getActivityLogsByInternship(internshipId)
                _uiState.value = if (activity.isEmpty()){
                    InternshipUiState.Empty
                }else{
                    InternshipUiState.SuccessActivityList(activity)
                }
            }catch (e: Exception){
                _uiState.value = InternshipUiState.Error(
                    e.message ?: "Erro ao carregar estágios"
                )
            }
        }
    }
}