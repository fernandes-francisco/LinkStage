package turmaA.grupoB.LinkStage.ui.orientador

import androidx.compose.ui.graphics.Color
import turmaA.grupoB.LinkStage.ui.admin.AdminStudent
import turmaA.grupoB.LinkStage.ui.aluno.activity.ActiveInternship
import turmaA.grupoB.LinkStage.ui.aluno.activity.ActivityLog
import turmaA.grupoB.LinkStage.ui.aluno.activity.ActivityLogStatus
import turmaA.grupoB.LinkStage.ui.aluno.activity.CheckpointFile
import java.time.LocalDate

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
    val location: String = "Viana do Castelo, PT",
    val duration: String = "6 Meses",
    val type: String = "Remoto",
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

val sampleMentorInternships = listOf(
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
        duration = "6 Meses",
        type = "Remoto",
        aboutCompany = "Lojinha de compras para os ricos e afortunados, queremos estagiário para servir de escravo.",
        responsibilities = listOf(
            "Realizar a prototipagem da app web.",
            "Colaborar com a equipa, com o objetivo cruzar habilidades.",
            "Desenvolver o nosso sistema de criação de dashboards.",
        ),
        requirements = listOf(
            "Experiência com Figma e prototipagem interativa.",
            "Portfólio do UI para demonstração.",
            "Comunicação excelente escrita e verbal em Inglês.",
        ),
        benefits = listOf("Passe de Transporte Público", "Programa de Mentoria", "Mercado Competitivo"),
        isBusinessInternship = true
    ),
    MentorInternship(
        id = "i2",
        offerTitle = "Designer de Produto",
        businessInstitutionName = "Viana S.T.Arts",
        schoolInstitutionName = "Uni. de Aveiro",
        logoInitial = "V",
        logoColor = Color(0xFF212121),
        endDate = LocalDate.of(2026, 6, 27),
        studentName = "Francisco Fernandes",
        studentId = "s2",
        location = "Aveiro, PT",
        duration = "4 Meses",
        type = "Presencial",
        aboutCompany = "A Universidade de Aveiro é uma instituição de ensino superior público que se destaca pela qualidade da investigação e inovação.",
        responsibilities = listOf(
            "Desenvolver interfaces para plataforma de e-learning.",
            "Participar em sessões de design thinking.",
        ),
        requirements = listOf(
            "Conhecimentos de design centrado no utilizador.",
            "Experiência com ferramentas de prototipagem.",
        ),
        benefits = listOf("Certificado de Estágio", "Acesso a Laboratórios"),
        isBusinessInternship = false
    ),
)

val sampleMentorStudents = listOf(
    AdminStudent(
        "s1", "Tiago Rodrigues", "tiago@estg.ipvc.pt", "912000001",
        "ESTG-IPVC", "ESTG-IPVC", "Eng. Informática", 14.5f,
        "2h atrás", true, "Continente", 2, "TR", 0,
        skills = listOf("Kotlin", "Jetpack Compose", "UI/UX Design", "Figma"),
    ),
    AdminStudent(
        "s2", "Francisco Fernandes", "francisco@ese.ipvc.pt", "912000002",
        "ESE-IPVC", "ESE-IPVC", "Educação", 13.0f,
        "3h atrás", false, "", 1, "FF", 1,
        skills = listOf("Pedagogia", "Gestão de Sala", "Comunicação"),
    ),
)

val sampleMentorActivityLogs = listOf(
    ActivityLog(
        "1", "Ponto de Controlo 1", "Foquei-me em desenhar as primeiras mockups.",
        LocalDate.of(2026, 1, 31), ActivityLogStatus.COMPLETED,
        "Viana S.T.Arts", "V", Color(0xFF212121),
        requirements = listOf("PPT com o trabalho realizado.", "Relatório atualizado.", "Documentação adicional."),
        hasSubmitted = true,
        submittedAt = LocalDate.of(2026, 1, 31),
        submittedFiles = listOf(
            CheckpointFile("f1", "PPT ponto de controlo"),
            CheckpointFile("f2", "relatório atualizado"),
        ),
    ),
    ActivityLog(
        "2", "Ponto de Controlo 2", "Foquei-me em desenhar as primeiras mockups.",
        LocalDate.of(2026, 5, 5), ActivityLogStatus.PENDING,
        "Viana S.T.Arts", "V", Color(0xFF212121),
        requirements = listOf("PPT com o trabalho realizado.", "Relatório atualizado.", "Documentação adicional."),
        hasSubmitted = false,
        submittedFiles = emptyList(),
    ),
)

val sampleStudentInternship = ActiveInternship(
    id = "int1",
    title = "Designer de Produto",
    startDate = LocalDate.of(2025, 10, 1),
    endDate = LocalDate.of(2026, 6, 1),
    activityLogs = sampleMentorActivityLogs,
)

val sampleEvaluation = InternshipEvaluation(
    internshipId = "int1",
    internshipType = InternshipType.COMPANY_SCHOOL,
    state = EvaluationState.READY_FOR_FINAL,
    companyResponsibleGrade = 16.5f,
    companyResponsibleObservation = "Excelente desempenho técnico.",
    companyResponsibleName = "Ana Costa",
    companyMentorGrade = 15.0f,
    companyMentorObservation = "Bom trabalho em equipa.",
    companyMentorName = "Prof. Tiago Alexandre",
    schoolMentorName = "Prof. Carvalho",
    hasSeenNotification = false,
)

// endregion

// region Formatting

fun formatInternshipDate(date: LocalDate): String {
    val months = listOf(
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro",
    )
    return "${date.dayOfMonth} de ${months[date.monthValue - 1]} de ${date.year}"
}

fun formatCheckpointDate(date: LocalDate): String {
    val months = listOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez")
    return "${months[date.monthValue - 1]} ${String.format("%02d", date.dayOfMonth)}, ${date.year}"
}

fun formatCheckpointDateLong(date: LocalDate): String {
    val months = listOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez")
    return "${date.dayOfMonth} ${months[date.monthValue - 1]} ${date.year}"
}

// endregion
