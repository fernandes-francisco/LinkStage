package turmaA.grupoB.LinkStage.ui.admin

import androidx.compose.ui.graphics.Color
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.MediumBlue

data class AdminStudent(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val institution: String,
    val institutionCode: String,
    val course: String,
    val gpa: Float,
    val registeredAgo: String,
    val hasActiveInternship: Boolean,
    val internshipCompany: String = "",
    val applicationCount: Int = 0,
    val avatarInitials: String,
    val avatarColorIndex: Int = 0,
    val skills: List<String> = emptyList(),
)

data class AdminMentor(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val institution: String,
    val department: String,
    val registeredAgo: String,
    val activeStudentsCount: Int,
    val avatarInitials: String,
    val avatarColorIndex: Int = 0,
)

data class AdminInstitution(
    val id: String,
    val name: String,
    val code: String,
    val logoInitial: String,
    val logoColor: Color,
    val type: String,
    val location: String,
    val registeredAgo: String,
    val studentsCount: Int,
    val mentorsCount: Int,
    val activeInternshipsCount: Int,
    val email: String = "",
    val website: String = "",
)

val avatarColors = listOf(LightBlue, DarkBlue, MediumBlue)

val sampleStudents = listOf(
    AdminStudent(
        "s1", "Tiago Rodrigues", "tiago@estg.ipvc.pt", "912000001",
        "ESTG-IPVC", "ESTG-IPVC", "Eng. Informática", 14.5f,
        "2h atrás", true, "Viana S.T.Arts", 3, "TR", 0,
    ),
    AdminStudent(
        "s2", "João Pinto", "joao@ese.ipvc.pt", "912000002",
        "ESE-IPVC", "ESE-IPVC", "Educação Básica", 13.2f,
        "3h atrás", false, "", 1, "JP", 1,
    ),
    AdminStudent(
        "s3", "Ana Costa", "ana@estg.ipvc.pt", "912000003",
        "ESTG-IPVC", "ESTG-IPVC", "Design", 15.0f,
        "1d atrás", true, "Pingo Doce", 2, "AC", 2,
    ),
)

val sampleMentors = listOf(
    AdminMentor(
        "m1", "Prof. Carvalho", "carvalho@ipvc.pt", "910000001",
        "ESTG-IPVC", "Informática", "1d atrás", 3, "PC", 0,
    ),
    AdminMentor(
        "m2", "Prof. Santos", "santos@ese.ipvc.pt", "910000002",
        "ESE-IPVC", "Educação", "2d atrás", 1, "PS", 1,
    ),
)

val sampleInstitutions = listOf(
    AdminInstitution(
        "i1", "Uni. de Aveiro", "UA", "U", Color(0xFF1565C0),
        "Instituição", "Aveiro", "2h atrás", 45, 8, 12,
        "geral@ua.pt", "ua.pt",
    ),
    AdminInstitution(
        "i2", "Pingo Doce", "PD", "P", Color(0xFF388E3C),
        "Empresa", "Lisboa", "3h atrás", 12, 2, 5,
        "estagios@pingodoce.pt", "pingodoce.pt",
    ),
)
