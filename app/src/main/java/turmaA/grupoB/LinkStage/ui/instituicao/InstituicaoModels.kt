package turmaA.grupoB.LinkStage.ui.instituicao

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import turmaA.grupoB.LinkStage.R
import turmaA.grupoB.LinkStage.ui.aluno.home.ApplicationStatus
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.Red

enum class InternshipOrigin { SCHOOL, COMPANY }

data class InstitutionInternship(
    val id: String,
    val studentName: String,
    val studentAvatarInitials: String,
    val studentAvatarColorIndex: Int = 0,
    val offerTitle: String,
    val origin: InternshipOrigin = InternshipOrigin.SCHOOL,
    val schoolMentorName: String = "",
    val companyMentorName: String = "",
    val progressPercent: Int,
    val status: InternshipStatus,
) {
    val mentorName: String
        get() = when (origin) {
            InternshipOrigin.SCHOOL -> schoolMentorName
            InternshipOrigin.COMPANY -> listOfNotNull(
                schoolMentorName.ifEmpty { null },
                companyMentorName.ifEmpty { null },
            ).joinToString(" · ")
        }

    val hasMentor: Boolean
        get() = when (origin) {
            InternshipOrigin.SCHOOL -> schoolMentorName.isNotEmpty()
            InternshipOrigin.COMPANY -> schoolMentorName.isNotEmpty() && companyMentorName.isNotEmpty()
        }

    val needsSchoolMentor: Boolean get() = schoolMentorName.isEmpty()
    val needsCompanyMentor: Boolean get() = origin == InternshipOrigin.COMPANY && companyMentorName.isEmpty()
}

enum class InternshipStatus {
    IN_PROGRESS,
    PENDING_REVIEW,
    COMPLETED,
    NO_MENTOR,
}

data class InstitutionMentorItem(
    val id: String,
    val name: String,
    val avatarInitials: String,
    val avatarColorIndex: Int = 0,
    val institution: String,
    val status: MentorStatus,
)

enum class MentorStatus {
    ACTIVE,
    INACTIVE,
    NO_STUDENTS,
}

val sampleInstitutionInternships = listOf(
    InstitutionInternship(
        id = "int1", studentName = "Tomás Silva", studentAvatarInitials = "TS", studentAvatarColorIndex = 0,
        offerTitle = "UI/UX Designer", origin = InternshipOrigin.SCHOOL,
        schoolMentorName = "Prof. Miguel Azevedo",
        progressPercent = 70, status = InternshipStatus.IN_PROGRESS,
    ),
    InstitutionInternship(
        id = "int2", studentName = "Francisco Fern.", studentAvatarInitials = "FF", studentAvatarColorIndex = 1,
        offerTitle = "Web Developer", origin = InternshipOrigin.COMPANY,
        schoolMentorName = "Prof. Tiago Alex.", companyMentorName = "Eng. Rui Sousa",
        progressPercent = 40, status = InternshipStatus.IN_PROGRESS,
    ),
    InstitutionInternship(
        id = "int3", studentName = "Ana Costa", studentAvatarInitials = "AC", studentAvatarColorIndex = 2,
        offerTitle = "Data Analyst", origin = InternshipOrigin.COMPANY,
        schoolMentorName = "Prof. Tiago Alex.", companyMentorName = "Dr. Marta Lopes",
        progressPercent = 100, status = InternshipStatus.PENDING_REVIEW,
    ),
    InstitutionInternship(
        id = "int4", studentName = "João Pinto", studentAvatarInitials = "JP", studentAvatarColorIndex = 1,
        offerTitle = "Designer de Produto", origin = InternshipOrigin.SCHOOL,
        progressPercent = 0, status = InternshipStatus.NO_MENTOR,
    ),
    InstitutionInternship(
        id = "int5", studentName = "Ricardo Lopes", studentAvatarInitials = "RL", studentAvatarColorIndex = 0,
        offerTitle = "Programador Full-Stack", origin = InternshipOrigin.COMPANY,
        schoolMentorName = "Prof. Carvalho",
        progressPercent = 0, status = InternshipStatus.NO_MENTOR,
    ),
)

val sampleInstitutionMentors = listOf(
    InstitutionMentorItem("m1", "Miguel Azev.", "MA", 2, "ESTG-IPVC", MentorStatus.ACTIVE),
    InstitutionMentorItem("m2", "Miguel Azev.", "MA", 2, "ESTG-IPVC", MentorStatus.INACTIVE),
    InstitutionMentorItem("m3", "Miguel Azev.", "MA", 2, "ESTG-IPVC", MentorStatus.NO_STUDENTS),
)

@Composable
fun internshipStatusLabel(status: InternshipStatus): String = when (status) {
    InternshipStatus.IN_PROGRESS -> stringResource(R.string.status_in_progress)
    InternshipStatus.PENDING_REVIEW -> stringResource(R.string.status_pending_review)
    InternshipStatus.COMPLETED -> stringResource(R.string.status_completed)
    InternshipStatus.NO_MENTOR -> stringResource(R.string.status_no_mentor)
}

fun internshipStatusColor(status: InternshipStatus): Color = when (status) {
    InternshipStatus.IN_PROGRESS -> LightBlue
    InternshipStatus.PENDING_REVIEW -> Color(0xFFF5C518)
    InternshipStatus.COMPLETED -> Color(0xFF9E9E9E)
    InternshipStatus.NO_MENTOR -> Red
}

@Composable
fun mentorStatusLabel(status: MentorStatus): String = when (status) {
    MentorStatus.ACTIVE -> stringResource(R.string.mentor_status_active)
    MentorStatus.INACTIVE -> stringResource(R.string.mentor_status_inactive)
    MentorStatus.NO_STUDENTS -> stringResource(R.string.mentor_status_no_students)
}

fun mentorStatusColor(status: MentorStatus): Color = when (status) {
    MentorStatus.ACTIVE -> LightBlue
    MentorStatus.INACTIVE -> Color(0xFF9E9E9E)
    MentorStatus.NO_STUDENTS -> Red
}

data class InstitutionApplication(
    val id: String,
    val studentName: String,
    val studentAvatarInitials: String,
    val studentAvatarColorIndex: Int = 0,
    val institution: String,
    val course: String,
    val gpa: String,
    val email: String,
    val phone: String,
    val skills: List<String>,
    val personalStatement: String,
    val motivationLetterTitle: String,
    val motivationLetterBody: String,
    val hasMotivationLetter: Boolean,
    val status: ApplicationStatus,
)

val sampleInstitutionApplications = listOf(
    InstitutionApplication(
        "a1", "Miguel Azev.", "MA", 2, "ESTG-IPVC", "Eng. Informatica", "14,7",
        "miguel@estg.ipvc.pt", "912000001",
        listOf("Python", "Trabalho em grupo", "Gestao de Projetos", "IA", "C++", "Java"),
        "Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat.",
        "Lorem ipsum dolor sit.",
        "Lorem ipsum dolor sit amet consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
        true, ApplicationStatus.ACCEPTED,
    ),
    InstitutionApplication(
        "a2", "Miguel Azev.", "MA", 2, "ESTG-IPVC", "Eng. Informatica", "13,2",
        "miguel2@estg.ipvc.pt", "912000002",
        listOf("Python", "Java"),
        "Declaracao pessoal do candidato.",
        "Carta de motivacao.", "Conteudo da carta de motivacao do candidato.",
        true, ApplicationStatus.PENDING,
    ),
    InstitutionApplication(
        "a3", "Miguel Azev.", "MA", 2, "ESTG-IPVC", "Eng. Informatica", "12,0",
        "miguel3@estg.ipvc.pt", "912000003",
        listOf("C++"),
        "Declaracao pessoal.",
        "Carta de motivacao.", "Conteudo da carta.",
        true, ApplicationStatus.PENDING,
    ),
)
