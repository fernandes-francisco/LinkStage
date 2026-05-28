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
    val institutionName: String,
    val institutionType: String,
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
)

data class InternshipEvaluation(
    val internshipId: String,
    val schoolMentorName: String,
    val schoolMentorObservation: String = "",
    val schoolMentorGrade: String = "",
    val companyMentorName: String = "",
    val companyMentorObservation: String = "",
    val companyMentorGrade: String = "",
    val finalGrade: Float? = null,
    val isCompleted: Boolean = false,
)

// region Sample data

val sampleMentorInternships = listOf(
    MentorInternship(
        id = "i1",
        offerTitle = "UI/UX Designer",
        institutionName = "Continente",
        institutionType = "Instituição",
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
    ),
    MentorInternship(
        id = "i2",
        offerTitle = "Designer de Produto",
        institutionName = "Uni. de Aveiro",
        institutionType = "Instituto Escolar",
        logoInitial = "U",
        logoColor = Color(0xFF1565C0),
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
    schoolMentorName = "Prof. Carvalho",
    companyMentorName = "Ana Costa",
    isCompleted = false,
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
