package turmaA.grupoB.LinkStage.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import turmaA.grupoB.LinkStage.data.remote.model.application.ApplicationModel
import turmaA.grupoB.LinkStage.data.remote.model.enums.ApplicationStatus
import turmaA.grupoB.LinkStage.data.remote.model.enums.InternshipStatus
import turmaA.grupoB.LinkStage.data.remote.model.enums.OfferStatus
import turmaA.grupoB.LinkStage.data.remote.model.enums.UserRole
import turmaA.grupoB.LinkStage.data.remote.model.institution.InstitutionModel
import turmaA.grupoB.LinkStage.data.remote.model.internship.ActivityLogModel
import turmaA.grupoB.LinkStage.data.remote.model.internship.AssignSupervisorInput
import turmaA.grupoB.LinkStage.data.remote.model.internship.CreateActivityLogInput
import turmaA.grupoB.LinkStage.data.remote.model.internship.CreateInternshipInput
import turmaA.grupoB.LinkStage.data.remote.model.internship.InternshipModel
import turmaA.grupoB.LinkStage.data.remote.model.offer.CreateOfferInput
import turmaA.grupoB.LinkStage.data.remote.model.offer.InternshipOfferModel
import turmaA.grupoB.LinkStage.data.remote.model.offer.UpdateOfferInput
import turmaA.grupoB.LinkStage.data.remote.model.user.ProfileModel
import turmaA.grupoB.LinkStage.data.remote.model.auth.SignInInput
import turmaA.grupoB.LinkStage.data.remote.model.auth.SignUpInput
import turmaA.grupoB.LinkStage.data.remote.model.user.UpdateProfileInput
import turmaA.grupoB.LinkStage.data.repository.application.ApplicationRepositoryInterface
import turmaA.grupoB.LinkStage.data.repository.auth.AuthRepositoryInterface
import turmaA.grupoB.LinkStage.data.repository.institution.InstitutionRepositoryInterface
import turmaA.grupoB.LinkStage.data.repository.internship.InternshipRepositoryInterface
import turmaA.grupoB.LinkStage.data.repository.offer.OfferRepositoryInterface

@OptIn(ExperimentalCoroutinesApi::class)
class InstitutionHomeViewModelTest {

    @Test
    fun loadDashboardForCurrentUser_whenRepositoryDataExists_setsSuccessState() = runTest {
        val institution = institutionModel()
        val offer = offerModel(status = OfferStatus.PUBLISHED)
        val closedOffer = offerModel(id = "offer-2", status = OfferStatus.CLOSED)
        val application = applicationModel()
        val internshipInProgress = internshipModel(status = InternshipStatus.IN_PROGRESS, hasMentor = false)
        val internshipPending = internshipModel(
            id = "internship-2",
            status = InternshipStatus.PENDING_SUPERVISOR,
            hasMentor = true,
        )

        val repository = FakeInstitutionHomeRepositories(
            userId = "user-1",
            institution = institution,
            offers = listOf(offer, closedOffer),
            applications = mapOf(offer.id to listOf(application)),
            internships = listOf(internshipInProgress, internshipPending),
        )
        val viewModel = InstitutionHomeViewModel(
            authRepository = repository,
            institutionRepository = repository,
            offerRepository = repository,
            applicationRepository = repository,
            internshipRepository = repository,
            dispatcher = StandardTestDispatcher(testScheduler),
        )

        viewModel.loadDashboardForCurrentUser()
        advanceUntilIdle()

        val state = viewModel.dashboardUiState.value
        assertEquals(
            InstitutionDashboardUiState.Success(
                activeOffersCount = 1,
                applicationsCount = 1,
                activeInternshipsCount = 1,
                pendingEvaluationsCount = 1,
                noMentorCount = 1,
            ),
            state,
        )
    }

    @Test
    fun loadDashboardForCurrentUser_whenRepositoryThrows_setsHardcodedFallbackState() = runTest {
        val repository = FakeInstitutionHomeRepositories(
            userId = "user-1",
            institution = institutionModel(),
            shouldThrowOnDashboard = true,
        )
        val viewModel = InstitutionHomeViewModel(
            authRepository = repository,
            institutionRepository = repository,
            offerRepository = repository,
            applicationRepository = repository,
            internshipRepository = repository,
            dispatcher = StandardTestDispatcher(testScheduler),
        )

        viewModel.loadDashboardForCurrentUser()
        advanceUntilIdle()

        assertEquals(
            InstitutionDashboardUiState.Success(
                activeOffersCount = 5,
                applicationsCount = 12,
                activeInternshipsCount = 3,
                pendingEvaluationsCount = 1,
                noMentorCount = 1,
            ),
            viewModel.dashboardUiState.value,
        )
    }

    @Test
    fun loadDashboardForCurrentUser_whenUserIsNotLoggedIn_setsHardcodedFallbackState() = runTest {
        val repository = FakeInstitutionHomeRepositories(userId = null)
        val viewModel = InstitutionHomeViewModel(
            authRepository = repository,
            institutionRepository = repository,
            offerRepository = repository,
            applicationRepository = repository,
            internshipRepository = repository,
            dispatcher = StandardTestDispatcher(testScheduler),
        )

        viewModel.loadDashboardForCurrentUser()
        advanceUntilIdle()

        assertEquals(
            InstitutionDashboardUiState.Success(
                activeOffersCount = 5,
                applicationsCount = 12,
                activeInternshipsCount = 3,
                pendingEvaluationsCount = 1,
                noMentorCount = 1,
            ),
            viewModel.dashboardUiState.value,
        )
    }
}

private fun institutionModel(
        id: String = "institution-1",
        userId: String = "user-1",
    ) = InstitutionModel(
        id = id,
        userId = userId,
        name = "Instituição Teste",
        createdAt = "2026-01-01T00:00:00Z",
        updatedAt = "2026-01-01T00:00:00Z",
    )

    private fun offerModel(
        id: String = "offer-1",
        institutionId: String = "institution-1",
        status: OfferStatus = OfferStatus.PUBLISHED,
    ) = InternshipOfferModel(
        id = id,
        institutionId = institutionId,
        title = "Estágio $id",
        description = "Descrição",
        area = "Tecnologia",
        status = status,
        createdAt = "2026-01-01T00:00:00Z",
        updatedAt = "2026-01-01T00:00:00Z",
    )

    private fun applicationModel() = ApplicationModel(
        id = "application-1",
        offerId = "offer-1",
        studentId = "student-1",
        status = ApplicationStatus.PENDING,
        createdAt = "2026-01-01T00:00:00Z",
        updatedAt = "2026-01-01T00:00:00Z",
    )

    private fun internshipModel(
        id: String = "internship-1",
        status: InternshipStatus = InternshipStatus.IN_PROGRESS,
        hasMentor: Boolean = true,
    ) = InternshipModel(
        id = id,
        applicationId = "application-1",
        offerId = "offer-1",
        studentId = "student-1",
        institutionId = "institution-1",
        supervisorId = "supervisor-1".takeIf { hasMentor },
        title = "Estágio $id",
        status = status,
        createdAt = "2026-01-01T00:00:00Z",
        updatedAt = "2026-01-01T00:00:00Z",
    )

private class FakeInstitutionHomeRepositories(
    private val userId: String?,
    private val institution: InstitutionModel? = null,
    private val offers: List<InternshipOfferModel> = emptyList(),
    private val applications: Map<String, List<ApplicationModel>> = emptyMap(),
    private val internships: List<InternshipModel> = emptyList(),
    private val shouldThrowOnDashboard: Boolean = false,
) : AuthRepositoryInterface,
    InstitutionRepositoryInterface,
    OfferRepositoryInterface,
    ApplicationRepositoryInterface,
    InternshipRepositoryInterface {

    override suspend fun signUp(input: SignUpInput): ProfileModel = ProfileModel(
        id = userId ?: "user-1",
        name = "Utilizador Teste",
        email = "teste@linkstage.pt",
        role = UserRole.INSTITUTION,
        createdAt = "2026-01-01T00:00:00Z",
        updatedAt = "2026-01-01T00:00:00Z",
    )

    override suspend fun signIn(input: SignInInput) = Unit

    override suspend fun signOut() = Unit

    override fun getCurrentUserId(): String? = userId

    override fun isUserLoggedIn(): Boolean = userId != null

    override suspend fun getCurrentUserProfile(): ProfileModel? = null

    override suspend fun updateProfile(
        userId: String,
        input: UpdateProfileInput,
    ): ProfileModel = ProfileModel(
        id = userId,
        name = input.name ?: "Utilizador Teste",
        email = "teste@linkstage.pt",
        role = UserRole.INSTITUTION,
        createdAt = "2026-01-01T00:00:00Z",
        updatedAt = "2026-01-01T00:00:00Z",
    )

    override suspend fun getInstitutions(): List<InstitutionModel> = institution?.let(::listOf).orEmpty()

    override suspend fun getInstitutionById(institutionId: String): InstitutionModel? =
        institution?.takeIf { it.id == institutionId }

    override suspend fun getInstitutionByUserId(userId: String): InstitutionModel? {
        if (shouldThrowOnDashboard) throw IllegalStateException("Erro")
        return institution?.takeIf { it.userId == userId }
    }

    override suspend fun getPublishedOffers(): List<InternshipOfferModel> = offers

    override suspend fun getOffersByInstitution(institutionId: String): List<InternshipOfferModel> {
        if (shouldThrowOnDashboard) throw IllegalStateException("Erro")
        return offers.filter { it.institutionId == institutionId }
    }

    override suspend fun getOfferById(offerId: String): InternshipOfferModel? =
        offers.firstOrNull { it.id == offerId }

    override suspend fun createOffer(input: CreateOfferInput): InternshipOfferModel = offerModel()

    override suspend fun updateOffer(offerId: String, input: UpdateOfferInput): InternshipOfferModel =
        offerModel(id = offerId)

    override suspend fun closeOffer(offerId: String): InternshipOfferModel =
        offerModel(id = offerId, status = OfferStatus.CLOSED)

    override suspend fun markOfferAsRemoved(offerId: String): InternshipOfferModel =
        offerModel(id = offerId, status = OfferStatus.REMOVED)

    override suspend fun getApplicationById(applicationId: String): ApplicationModel? = null

    override suspend fun getApplicationsByOffer(offerId: String): List<ApplicationModel> {
        if (shouldThrowOnDashboard) throw IllegalStateException("Erro")
        return applications[offerId].orEmpty()
    }

    override suspend fun getApplicationsByStudent(studentId: String): List<ApplicationModel> = emptyList()

    override suspend fun getApplicationsByStatus(status: ApplicationStatus): List<ApplicationModel> = emptyList()

    override suspend fun createApplication(input: turmaA.grupoB.LinkStage.data.remote.model.application.CreateApplicationInput): ApplicationModel =
        applicationModel()

    override suspend fun updateApplicationDecision(
        applicationId: String,
        input: turmaA.grupoB.LinkStage.data.remote.model.application.UpdateApplicationDecisionInput,
    ): ApplicationModel = applicationModel()

    override suspend fun acceptApplication(applicationId: String): ApplicationModel = applicationModel()

    override suspend fun rejectApplication(applicationId: String, rejectionReason: String): ApplicationModel =
        applicationModel()

    override suspend fun getInternships(): List<InternshipModel> = internships

    override suspend fun getInternshipById(internshipId: String): InternshipModel? =
        internships.firstOrNull { it.id == internshipId }

    override suspend fun getInternshipsByStudent(studentId: String): List<InternshipModel> = emptyList()

    override suspend fun getInternshipsByInstitution(institutionId: String): List<InternshipModel> {
        if (shouldThrowOnDashboard) throw IllegalStateException("Erro")
        return internships.filter { it.institutionId == institutionId }
    }

    override suspend fun getInternshipsByStatus(status: InternshipStatus): List<InternshipModel> = emptyList()

    override suspend fun createInternship(input: CreateInternshipInput): InternshipModel = internshipModel()

    override suspend fun assignSupervisor(
        internshipId: String,
        input: AssignSupervisorInput,
    ): InternshipModel = internshipModel(id = internshipId)

    override suspend fun getActivityLogsByInternship(internshipId: String): List<ActivityLogModel> = emptyList()

    override suspend fun createActivityLog(input: CreateActivityLogInput): ActivityLogModel =
        ActivityLogModel(
            id = "log-1",
            internshipId = "internship-1",
            studentId = "student-1",
            description = "Log",
            activityDate = "2026-01-01",
            createdAt = "2026-01-01T00:00:00Z",
        )
}
