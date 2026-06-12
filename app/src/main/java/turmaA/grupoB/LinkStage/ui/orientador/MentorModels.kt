package turmaA.grupoB.LinkStage.ui.orientador

import android.content.Context
import androidx.compose.ui.graphics.Color
import turmaA.grupoB.LinkStage.R
import turmaA.grupoB.LinkStage.ui.admin.AdminStudent
import turmaA.grupoB.LinkStage.ui.aluno.activity.ActiveInternship
import turmaA.grupoB.LinkStage.ui.aluno.activity.ActivityLog
import turmaA.grupoB.LinkStage.ui.aluno.activity.ActivityLogStatus
import turmaA.grupoB.LinkStage.ui.aluno.activity.CheckpointFile
import java.time.LocalDate

enum class CheckpointCreatedBy { STUDENT, MENTOR }

data class CheckpointViewer(
    val viewerId: String,
    val viewerName: String,
    val viewerRole: String,
    val viewedAt: String,
)

data class MentorInternship(
    val id: String,
    val offerTitle: String,
    val businessInstitutionName: String,
    val schoolInstitutionName: String,
    val logoInitial: String,
    val logoColor: Color,
    val endDate: LocalDate,
    val studentName: String,
    val studentId: String,
    val location: String = "",
    val duration: String = "",
    val type: String = "",
    val aboutCompany: String = "",
    val responsibilities: List<String> = emptyList(),
    val requirements: List<String> = emptyList(),
    val benefits: List<String> = emptyList(),
    val isBusinessInternship: Boolean = true,
)

enum class InternshipType { COMPANY_SCHOOL, SCHOOL_ONLY }

enum class EvaluationState {
    PENDING,
    PARTIAL,
    READY_FOR_FINAL,
    COMPLETED,
}

data class InternshipEvaluation(
    val internshipId: String,
    val internshipType: InternshipType = InternshipType.COMPANY_SCHOOL,
    val state: EvaluationState = EvaluationState.PENDING,

    val companyResponsibleGrade: Float? = null,
    val companyResponsibleObservation: String? = null,
    val companyResponsibleName: String = "",

    val companyMentorGrade: Float? = null,
    val companyMentorObservation: String? = null,
    val companyMentorName: String = "",

    val institutionGrade: Float? = null,
    val institutionObservation: String? = null,
    val institutionName: String = "",

    val schoolMentorGrade: Float? = null,
    val schoolMentorObservation: String? = null,
    val schoolMentorName: String = "",

    val hasSeenNotification: Boolean = false,
)

fun calculateEvaluationState(eval: InternshipEvaluation): EvaluationState {
    return when (eval.internshipType) {
        InternshipType.COMPANY_SCHOOL -> when {
            eval.schoolMentorGrade != null -> EvaluationState.COMPLETED
            eval.companyResponsibleGrade != null && eval.companyMentorGrade != null
                -> EvaluationState.READY_FOR_FINAL
            eval.companyResponsibleGrade != null || eval.companyMentorGrade != null
                -> EvaluationState.PARTIAL
            else -> EvaluationState.PENDING
        }
        InternshipType.SCHOOL_ONLY -> when {
            eval.schoolMentorGrade != null -> EvaluationState.COMPLETED
            eval.institutionGrade != null -> EvaluationState.READY_FOR_FINAL
            else -> EvaluationState.PENDING
        }
    }
}

// region Sample data

fun sampleMentorInternships(context: Context) = listOf(
    MentorInternship(
        id = "i1",
        offerTitle = "UI/UX Designer",
        businessInstitutionName = "Continente",
        schoolInstitutionName = "ESTG - IPVC",
        logoInitial = "C",
        logoColor = Color(0xFFE53935),
        endDate = LocalDate.of(2026, 5, 5),
        studentName = "Tiago Rodrigues",
        studentId = "s1",
        location = "Viana do Castelo, PT",
        duration = context.getString(R.string.mock_duration_6months),
        type = context.getString(R.string.mock_type_remote),
        aboutCompany = context.getString(R.string.mock_company_desc_continente),
        responsibilities = listOf(
            context.getString(R.string.mock_resp_prototyping),
            context.getString(R.string.mock_resp_collaborate),
            context.getString(R.string.mock_resp_dashboards),
        ),
        requirements = listOf(
            context.getString(R.string.mock_req_figma),
            context.getString(R.string.mock_req_portfolio),
            context.getString(R.string.mock_req_english),
        ),
        benefits = listOf(
            context.getString(R.string.mock_benefit_transport),
            context.getString(R.string.mock_benefit_mentoring),
            context.getString(R.string.mock_benefit_competitive),
        ),
        isBusinessInternship = true
    ),
    MentorInternship(
        id = "i2",
        offerTitle = context.getString(R.string.mock_product_designer),
        businessInstitutionName = "Viana S.T.Arts",
        schoolInstitutionName = "Uni. de Aveiro",
        logoInitial = "V",
        logoColor = Color(0xFF212121),
        endDate = LocalDate.of(2026, 6, 27),
        studentName = "Francisco Fernandes",
        studentId = "s2",
        location = "Aveiro, PT",
        duration = context.getString(R.string.mock_duration_4months),
        type = context.getString(R.string.mock_type_onsite),
        aboutCompany = context.getString(R.string.mock_company_desc_uaveiro),
        responsibilities = listOf(
            context.getString(R.string.mock_resp_elearning),
            context.getString(R.string.mock_resp_design_thinking),
        ),
        requirements = listOf(
            context.getString(R.string.mock_req_user_centered),
            context.getString(R.string.mock_req_prototyping_tools),
        ),
        benefits = listOf(
            context.getString(R.string.mock_benefit_certificate),
            context.getString(R.string.mock_benefit_labs),
        ),
        isBusinessInternship = false
    ),
)

fun sampleMentorStudents(context: Context) = listOf(
    AdminStudent(
        "s1", "Tiago Rodrigues", "tiago@estg.ipvc.pt", "912000001",
        "ESTG-IPVC", "ESTG-IPVC", context.getString(R.string.mock_course_cs), 14.5f,
        context.getString(R.string.mock_time_2h_ago), true, "Continente", 2, "TR", 0,
        skills = listOf("Kotlin", "Jetpack Compose", "UI/UX Design", "Figma"),
    ),
    AdminStudent(
        "s2", "Francisco Fernandes", "francisco@ese.ipvc.pt", "912000002",
        "ESE-IPVC", "ESE-IPVC", context.getString(R.string.mock_course_education), 13.0f,
        context.getString(R.string.mock_time_3h_ago), false, "", 1, "FF", 1,
        skills = listOf(
            context.getString(R.string.mock_skill_pedagogy),
            context.getString(R.string.mock_skill_classroom_mgmt),
            context.getString(R.string.mock_skill_communication),
        ),
    ),
)

fun sampleMentorActivityLogs(context: Context) = listOf(
    ActivityLog(
        "1", context.getString(R.string.mock_checkpoint_1), context.getString(R.string.mock_checkpoint_desc_mockups),
        LocalDate.of(2026, 1, 31), ActivityLogStatus.COMPLETED,
        "Viana S.T.Arts", "V", Color(0xFF212121),
        requirements = listOf(
            context.getString(R.string.mock_req_ppt),
            context.getString(R.string.mock_req_report_updated_short),
            context.getString(R.string.mock_req_additional_docs_short),
        ),
        hasSubmitted = true,
        submittedAt = LocalDate.of(2026, 1, 31),
        submittedFiles = listOf(
            CheckpointFile("f1", context.getString(R.string.mock_ppt_checkpoint_file)),
            CheckpointFile("f2", context.getString(R.string.mock_report_updated_file)),
        ),
        createdBy = "STUDENT",
        viewers = listOf(
            CheckpointViewer("m1", "Prof. Carvalho", context.getString(R.string.eval_role_school_mentor_short), context.getString(R.string.mock_today_at, "14:32")),
            CheckpointViewer("i1", "Viana S.T.Arts", context.getString(R.string.eval_role_institution), context.getString(R.string.mock_yesterday_at, "09:15")),
        ),
    ),
    ActivityLog(
        "2", context.getString(R.string.mock_interim_report), context.getString(R.string.mock_interim_report_desc),
        LocalDate.of(2026, 3, 15), ActivityLogStatus.PENDING,
        "Viana S.T.Arts", "V", Color(0xFF212121),
        requirements = listOf(
            context.getString(R.string.mock_req_ppt),
            context.getString(R.string.mock_req_report_updated_short),
            context.getString(R.string.mock_req_additional_docs_short),
        ),
        hasSubmitted = false,
        submittedFiles = emptyList(),
        createdBy = "MENTOR",
        createdByName = "Prof. Carvalho",
        viewers = emptyList(),
    ),
)

fun sampleStudentInternship(context: Context) = ActiveInternship(
    id = "int1",
    title = context.getString(R.string.mock_product_designer),
    startDate = LocalDate.of(2025, 10, 1),
    endDate = LocalDate.of(2026, 6, 1),
    activityLogs = sampleMentorActivityLogs(context),
)

fun sampleEvaluation(context: Context) = InternshipEvaluation(
    internshipId = "int1",
    internshipType = InternshipType.COMPANY_SCHOOL,
    state = EvaluationState.READY_FOR_FINAL,
    companyResponsibleGrade = 16.5f,
    companyResponsibleObservation = context.getString(R.string.mock_eval_excellent),
    companyResponsibleName = "Ana Costa",
    companyMentorGrade = 15.0f,
    companyMentorObservation = context.getString(R.string.mock_eval_good_teamwork),
    companyMentorName = "Prof. Tiago Alexandre",
    schoolMentorName = "Prof. Carvalho",
    hasSeenNotification = false,
)

// endregion

// region Formatting

fun formatInternshipDate(date: LocalDate, context: Context): String {
    val months = context.resources.getStringArray(R.array.months_full)
    return context.getString(R.string.date_format_full, date.dayOfMonth, months[date.monthValue - 1], date.year)
}

fun formatCheckpointDate(date: LocalDate, context: Context): String {
    val months = context.resources.getStringArray(R.array.months_short)
    return "${months[date.monthValue - 1]} ${String.format("%02d", date.dayOfMonth)}, ${date.year}"
}

fun formatCheckpointDateLong(date: LocalDate, context: Context): String {
    val months = context.resources.getStringArray(R.array.months_short)
    return "${date.dayOfMonth} ${months[date.monthValue - 1]} ${date.year}"
}

// endregion
