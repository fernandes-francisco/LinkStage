package turmaA.grupoB.LinkStage.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ApplyViewModel : ViewModel() {

    var currentStep by mutableStateOf(0)

    // Step 0
    var fullName by mutableStateOf("Tomás Silva")
    var email by mutableStateOf("tomas.silva@ipvc.pt")
    var phone by mutableStateOf("939696769")
    var course by mutableStateOf("Eng. Informática")
    var institution by mutableStateOf("IPVC")
    var gpa by mutableStateOf("14,7")

    // Step 1
    var personalStatement by mutableStateOf("")
    var userSkills by mutableStateOf(listOf("Python", "Trabalho em grupo", "Gestão de Projetos", "IA"))
    var motivationFile by mutableStateOf<Uri?>(null)
    var motivationFileName by mutableStateOf("")

    val isCvComplete: Boolean get() = userSkills.isNotEmpty()
    val isMotivationComplete: Boolean get() = motivationFile != null

    fun saveAsDraft() {
        // Placeholder — guardar em SharedPreferences ou Room
    }

    fun submitApplication() {
        // Placeholder — chamar repositório
    }

    fun addSkill(skill: String) {
        if (skill.isNotBlank() && skill !in userSkills) {
            userSkills = userSkills + skill
        }
    }

    fun removeSkill(skill: String) {
        userSkills = userSkills - skill
    }
}
