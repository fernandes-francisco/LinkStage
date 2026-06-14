package turmaA.grupoB.LinkStage.viewmodel.offerform

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class OfferFormViewModel : ViewModel() {

    // Step 0
    var schoolMentorId by mutableStateOf("")
    var schoolMentorName by mutableStateOf("")
    var companyMentorId by mutableStateOf("")
    var companyMentorName by mutableStateOf("")
    var title by mutableStateOf("")
    var category by mutableStateOf("")
    var description by mutableStateOf("")
    var isCompanyOffer by mutableStateOf(false)

    // Step 1
    var requirements by mutableStateOf("")
    var location by mutableStateOf("")
    var deadline by mutableStateOf("")

    var currentStep by mutableStateOf(0)

    val mentorSummary: String
        get() = when {
            isCompanyOffer -> listOfNotNull(
                schoolMentorName.ifBlank { null },
                companyMentorName.ifBlank { null },
            ).joinToString(" · ").ifEmpty { "" }
            else -> schoolMentorName
        }

    fun validateStep0(): Boolean =
        title.isNotBlank() && category.isNotBlank() && description.isNotBlank()

    fun validateStep1(): Boolean =
        location.isNotBlank() && deadline.isNotBlank() && isValidDate(deadline)

    fun isValidDate(date: String): Boolean {
        val regex = Regex("""^\d{2}/\d{2}/\d{4}$""")
        return regex.matches(date)
    }

    fun createOffer() {
        // TODO: call repository to create the offer in Supabase
    }
}
