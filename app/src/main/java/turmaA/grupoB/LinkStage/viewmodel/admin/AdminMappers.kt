package turmaA.grupoB.LinkStage.viewmodel.admin

import turmaA.grupoB.LinkStage.data.remote.model.application.ApplicationModel
import turmaA.grupoB.LinkStage.data.remote.model.institution.InstitutionModel
import turmaA.grupoB.LinkStage.data.remote.model.internship.InternshipModel
import turmaA.grupoB.LinkStage.data.remote.model.offer.InternshipOfferModel
import turmaA.grupoB.LinkStage.data.remote.model.user.ProfileModel
import turmaA.grupoB.LinkStage.data.remote.model.user.StudentModel
import turmaA.grupoB.LinkStage.data.remote.model.user.SupervisorModel
import turmaA.grupoB.LinkStage.data.remote.model.user.SupervisorSkillModel
import turmaA.grupoB.LinkStage.ui.admin.AdminInstitution
import turmaA.grupoB.LinkStage.ui.admin.AdminMentor
import turmaA.grupoB.LinkStage.ui.admin.AdminStudent
import turmaA.grupoB.LinkStage.ui.admin.InstitutionStatus
import turmaA.grupoB.LinkStage.ui.admin.avatarColors
import turmaA.grupoB.LinkStage.ui.admin.sampleInstitutions
import turmaA.grupoB.LinkStage.ui.admin.sampleMentors
import turmaA.grupoB.LinkStage.ui.admin.sampleStudents
import turmaA.grupoB.LinkStage.ui.aluno.offers.OfferDetail
import java.time.Instant
import java.time.temporal.ChronoUnit

data class AdminDashboardData(
    val pendingInstitutions: List<AdminInstitution>,
    val recentStudents: List<AdminStudent>,
    val recentMentors: List<AdminMentor>,
    val recentInstitutions: List<AdminInstitution>,
    val totalStudents: Int,
    val totalMentors: Int,
    val totalInstitutions: Int,
    val pendingApplications: Int,
    val activeInternships: Int,
)

data class AdminStudentDetailData(
    val student: AdminStudent,
    val applications: List<ApplicationModel>,
    val internships: List<InternshipModel>,
)

data class AdminMentorDetailData(
    val mentor: AdminMentor,
    val supervisedStudents: List<AdminStudent>,
)

data class AdminInstitutionDetailData(
    val institution: AdminInstitution,
    val relatedStudents: List<AdminStudent>,
    val relatedMentors: List<AdminMentor>,
)

data class AdminInternshipDetailData(
    val internship: InternshipModel,
    val offer: InternshipOfferModel,
    val institutionName: String,
    val student: AdminStudent,
    val mentor: AdminMentor?,
    val applications: List<ApplicationModel>,
) {
    val offerDetail: OfferDetail = offer.toOfferDetail(
        institutionName = institutionName,
        applicantsCount = applications.size,
    )
}

internal fun List<StudentModel>.toAdminStudents(
    profilesByUserId: Map<String, ProfileModel>,
    applicationsByStudentId: Map<String, List<ApplicationModel>>,
    internshipsByStudentId: Map<String, List<InternshipModel>>,
    offersById: Map<String, InternshipOfferModel>,
): List<AdminStudent> = map { student ->
    val profile = profilesByUserId[student.userId]
    val applications = applicationsByStudentId[student.id].orEmpty()
    val internships = internshipsByStudentId[student.id].orEmpty()
    val activeInternship = internships.firstOrNull { it.status.name == "IN_PROGRESS" }
    val internshipOffer = activeInternship?.offerId?.let { offersById[it] }

    AdminStudent(
        id = student.id,
        name = profile?.name ?: student.studentNumber,
        email = profile?.email ?: "",
        phone = profile?.phone ?: "",
        institution = student.course.substringBefore(" ").ifBlank { "Não indicada" },
        institutionCode = student.course.substringBefore(" ").uppercase().ifBlank { "NA" },
        course = student.course,
        gpa = student.averageGrade?.toFloat() ?: 0f,
        registeredAgo = student.createdAt.relativeTime(),
        hasActiveInternship = activeInternship != null,
        internshipCompany = internshipOffer?.title ?: "",
        applicationCount = applications.size,
        avatarInitials = profile?.name?.initials() ?: student.studentNumber.take(2).uppercase(),
        avatarColorIndex = student.id.hashCode().absMod(avatarColors.size),
        skills = student.cvData?.keys?.toList().orEmpty(),
    )
}

internal fun SupervisorModel.toAdminMentor(
    profile: ProfileModel?,
    activeInternshipsBySupervisorId: Map<String, List<InternshipModel>>,
    skills: List<SupervisorSkillModel>,
    colorIndex: Int = id.hashCode().absMod(avatarColors.size),
): AdminMentor = AdminMentor(
    id = id,
    name = profile?.name ?: "Mentor $id",
    email = profile?.email ?: "",
    phone = profile?.phone ?: "",
    institution = "Não indicada",
    department = department ?: specialty ?: "Não indicado",
    registeredAgo = createdAt.relativeTime(),
    activeStudentsCount = activeInternshipsBySupervisorId[id].orEmpty().count { it.status.name == "IN_PROGRESS" },
    avatarInitials = profile?.name?.initials() ?: id.take(2).uppercase(),
    avatarColorIndex = colorIndex,
    skills = skills.map { it.skill },
    supervisionAreas = listOfNotNull(department, specialty).distinct(),
    internalNote = if (acceptsNewInternships) "Disponível para novos estágios." else "Não aceita novos estágios.",
    isAvailable = acceptsNewInternships,
)

internal fun List<SupervisorModel>.toAdminMentors(
    profilesByUserId: Map<String, ProfileModel>,
    activeInternshipsBySupervisorId: Map<String, List<InternshipModel>>,
    skillsBySupervisorId: Map<String, List<SupervisorSkillModel>>,
): List<AdminMentor> = map { supervisor ->
    supervisor.toAdminMentor(
        profile = profilesByUserId[supervisor.userId],
        activeInternshipsBySupervisorId = activeInternshipsBySupervisorId,
        skills = skillsBySupervisorId[supervisor.id].orEmpty(),
    )
}

internal fun List<AdminMentor>.filterMentors(
    query: String,
    institution: String,
    department: String,
): List<AdminMentor> {
    val normalizedQuery = query.trim().lowercase()
    return filter { mentor ->
        val matchesQuery = normalizedQuery.isEmpty() ||
            mentor.name.contains(normalizedQuery, ignoreCase = true) ||
            mentor.email.contains(normalizedQuery, ignoreCase = true) ||
            mentor.department.contains(normalizedQuery, ignoreCase = true)
        val matchesInstitution = institution.isBlank() ||
            institution == "Todas" ||
            mentor.institution == institution
        val matchesDepartment = department.isBlank() ||
            department == "Todos" ||
            mentor.department == department
        matchesQuery && matchesInstitution && matchesDepartment
    }
}

internal fun List<AdminStudent>.filterStudents(
    query: String,
    institution: String,
    course: String,
    internshipStatus: String,
): List<AdminStudent> {
    val normalizedQuery = query.trim().lowercase()
    return filter { student ->
        val matchesQuery = normalizedQuery.isEmpty() ||
            student.name.contains(normalizedQuery, ignoreCase = true) ||
            student.email.contains(normalizedQuery, ignoreCase = true) ||
            student.course.contains(normalizedQuery, ignoreCase = true)
        val matchesInstitution = institution.isBlank() ||
            institution == "Todas" ||
            student.institution == institution
        val matchesCourse = course.isBlank() ||
            course == "Todos" ||
            student.course == course
        val matchesInternship = internshipStatus == "Todos" ||
            internshipStatus == "Com estágio" && student.hasActiveInternship ||
            internshipStatus == "Sem estágio" && !student.hasActiveInternship
        matchesQuery && matchesInstitution && matchesCourse && matchesInternship
    }
}

internal fun List<AdminInstitution>.filterInstitutions(
    query: String,
    type: String,
    location: String,
): List<AdminInstitution> {
    val normalizedQuery = query.trim().lowercase()
    return filter { institution ->
        val matchesQuery = normalizedQuery.isEmpty() ||
            institution.name.contains(normalizedQuery, ignoreCase = true) ||
            institution.code.contains(normalizedQuery, ignoreCase = true) ||
            institution.location.contains(normalizedQuery, ignoreCase = true)
        val matchesType = type.isBlank() ||
            type == "Todas" ||
            institution.type == type
        val matchesLocation = location.isBlank() ||
            location == "Todas" ||
            institution.location == location
        matchesQuery && matchesType && matchesLocation
    }
}

internal fun InstitutionModel.toAdminInstitution(
    profile: ProfileModel?,
    studentsCount: Int,
    mentorsCount: Int,
    activeInternshipsCount: Int,
): AdminInstitution = AdminInstitution(
    id = id,
    name = name,
    code = name.codeFromName(),
    logoInitial = name.firstOrNull()?.uppercaseChar()?.toString() ?: "I",
    logoColor = avatarColors[hashCode().absMod(avatarColors.size)],
    type = sector ?: "Instituição",
    location = address ?: "Não indicada",
    registeredAgo = createdAt.relativeTime(),
    studentsCount = studentsCount,
    mentorsCount = mentorsCount,
    activeInternshipsCount = activeInternshipsCount,
    email = profile?.email ?: "",
    website = website ?: "",
    status = InstitutionStatus.APPROVED,
    submittedAt = createdAt.relativeTime(),
)

internal fun InternshipOfferModel.toOfferDetail(
    institutionName: String,
    applicantsCount: Int,
): OfferDetail = OfferDetail(
    id = id,
    title = title,
    company = institutionName,
    logoInitial = institutionName.firstOrNull()?.uppercaseChar()?.toString() ?: "L",
    logoColor = avatarColors[hashCode().absMod(avatarColors.size)],
    location = location ?: "Não indicada",
    duration = "Não indicada",
    type = modality ?: "Não indicada",
    aboutCompany = description,
    responsibilities = requirements?.split('\n')?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList(),
    requirements = requirements?.split('\n')?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList(),
    benefits = listOfNotNull(salary?.let { "Salário: $it€" }),
    deadlineDays = 0,
    applicantsCount = applicantsCount,
)

internal fun fallbackDashboardData(): AdminDashboardData = AdminDashboardData(
    pendingInstitutions = sampleInstitutions.filter { it.status == InstitutionStatus.PENDING_APPROVAL },
    recentStudents = sampleStudents,
    recentMentors = sampleMentors,
    recentInstitutions = sampleInstitutions,
    totalStudents = sampleStudents.size,
    totalMentors = sampleMentors.size,
    totalInstitutions = sampleInstitutions.size,
    pendingApplications = 0,
    activeInternships = sampleStudents.count { it.hasActiveInternship },
)

internal fun fallbackInstitutions(): List<AdminInstitution> = sampleInstitutions

internal fun fallbackStudents(): List<AdminStudent> = sampleStudents

internal fun fallbackMentors(): List<AdminMentor> = sampleMentors

internal fun fallbackStudentDetail(studentId: String): AdminStudentDetailData {
    val student = sampleStudents.firstOrNull { it.id == studentId } ?: sampleStudents.first()
    return AdminStudentDetailData(
        student = student,
        applications = emptyList(),
        internships = emptyList(),
    )
}

internal fun fallbackMentorDetail(mentorId: String): AdminMentorDetailData {
    val mentor = sampleMentors.firstOrNull { it.id == mentorId } ?: sampleMentors.first()
    return AdminMentorDetailData(
        mentor = mentor,
        supervisedStudents = sampleStudents.filter { it.institution == mentor.institution },
    )
}

internal fun fallbackInstitutionDetail(institutionId: String): AdminInstitutionDetailData {
    val institution = sampleInstitutions.firstOrNull { it.id == institutionId } ?: sampleInstitutions.first()
    return AdminInstitutionDetailData(
        institution = institution,
        relatedStudents = sampleStudents.filter { it.institution == institution.code },
        relatedMentors = sampleMentors.filter { it.institution == institution.code },
    )
}

fun fallbackInternshipDetail(internshipId: String): AdminInternshipDetailData {
    val offer = InternshipOfferModel(
        id = internshipId,
        institutionId = "fallback",
        title = "Desenvolvimento de App Mobile",
        description = "Com o principal objetivo de realizar a reabilitação do antigo Matadouro Municipal de Viana do Castelo, visa transformar o edifício histórico num centro de ciência, arte e inovação.",
        area = "Mobile",
        location = "Viana do Castelo, PT",
        modality = "Remoto",
        salary = null,
        vacancies = 1,
        requirements = "Experiência com Figma e prototipagem interativa.\nPortfólio do UI para demonstração.\nComunicação excelente escrita e verbal em Inglês.",
        publishDate = null,
        deadline = null,
        createdAt = Instant.now().toString(),
        updatedAt = Instant.now().toString(),
    )

    return AdminInternshipDetailData(
        internship = InternshipModel(
            id = internshipId,
            applicationId = "fallback-application",
            offerId = internshipId,
            studentId = sampleStudents.first().id,
            institutionId = "fallback",
            supervisorId = sampleMentors.first().id,
            title = offer.title,
            companySupervisorName = null,
            startDate = null,
            endDate = null,
            createdAt = offer.createdAt,
            updatedAt = offer.updatedAt,
        ),
        offer = offer,
        institutionName = "Viana S.T.Arts",
        student = sampleStudents.first(),
        mentor = sampleMentors.first(),
        applications = emptyList(),
    )
}

internal fun String.relativeTime(): String {
    val instant = runCatching { Instant.parse(this) }.getOrNull() ?: return "recentemente"
    val minutes = ChronoUnit.MINUTES.between(instant, Instant.now()).coerceAtLeast(0)
    return when {
        minutes < 1 -> "agora"
        minutes < 60 -> "${minutes}m atrás"
        minutes < 1440 -> "${minutes / 60}h atrás"
        minutes < 43200 -> "${minutes / 1440}d atrás"
        else -> "há muito tempo"
    }
}

private fun String.initials(): String = split(' ')
    .filter { it.isNotBlank() }
    .take(2)
    .joinToString("") { it.first().uppercase() }

private fun String.codeFromName(): String = split(' ', '.', '-')
    .filter { it.isNotBlank() }
    .joinToString("") { it.first().uppercase().toString() }
    .take(4)
    .ifBlank { "I" }

private fun Int.absMod(modulus: Int): Int = if (modulus == 0) 0 else kotlin.math.abs(this) % modulus
