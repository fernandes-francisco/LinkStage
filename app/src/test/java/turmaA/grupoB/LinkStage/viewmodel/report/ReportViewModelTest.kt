package turmaA.grupoB.LinkStage.viewmodel.report

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.runner.Description
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import turmaA.grupoB.LinkStage.data.remote.model.enums.ReportStatus
import turmaA.grupoB.LinkStage.data.remote.model.report.FinalReportModel
import turmaA.grupoB.LinkStage.data.repository.ReportRepositoryInterface

@OptIn(ExperimentalCoroutinesApi::class)
class ReportViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeReportRepository
    private lateinit var viewModel: ReportViewModel

    @Before
    fun setup() {
        fakeRepository = FakeReportRepository()
        viewModel = ReportViewModel(fakeRepository)
    }

    @Test
    fun initialState_isIdle() {
        assertEquals(
            ReportUiState.Idle,
            viewModel.uiState.value
        )
    }

    // --- loadReports ---

    @Test
    fun loadReports_whenReportsExist_setsSuccessListState() = runTest {
        fakeRepository.reports = listOf(testReport)

        viewModel.loadReports()

        advanceUntilIdle()

        assertEquals(
            ReportUiState.SuccessList(listOf(testReport)),
            viewModel.uiState.value
        )
    }

    @Test
    fun loadReports_whenReportsDoNotExist_setsEmptyState() = runTest {
        fakeRepository.reports = emptyList()

        viewModel.loadReports()

        advanceUntilIdle()

        assertEquals(
            ReportUiState.Empty,
            viewModel.uiState.value
        )
    }

    @Test
    fun loadReports_whenRepositoryThrows_setsErrorState() = runTest {
        fakeRepository.shouldThrowOnGetReports = true

        viewModel.loadReports()

        advanceUntilIdle()

        assertEquals(
            ReportUiState.Error("Erro ao carregar relatórios."),
            viewModel.uiState.value
        )
    }

    // --- loadReportById ---

    @Test
    fun loadReportById_whenReportExists_setsSuccessState() = runTest {
        fakeRepository.reportById = testReport

        viewModel.loadReportById(testReport.id)

        advanceUntilIdle()

        assertEquals(
            ReportUiState.Success(testReport),
            viewModel.uiState.value
        )
    }

    @Test
    fun loadReportById_whenReportDoesNotExist_setsEmptyState() = runTest {
        fakeRepository.reportById = null

        viewModel.loadReportById(testReport.id)

        advanceUntilIdle()

        assertEquals(
            ReportUiState.Empty,
            viewModel.uiState.value
        )
    }

    @Test
    fun loadReportById_whenRepositoryThrows_setsErrorState() = runTest {
        fakeRepository.shouldThrowOnGetReportById = true

        viewModel.loadReportById(testReport.id)

        advanceUntilIdle()

        assertEquals(
            ReportUiState.Error("Erro ao carregar relatório."),
            viewModel.uiState.value
        )
    }

    // --- loadReportByInternship ---

    @Test
    fun loadReportByInternship_whenReportExists_setsSuccessState() = runTest {
        fakeRepository.reportByInternship = testReport

        viewModel.loadReportByInternship(testReport.internshipId)

        advanceUntilIdle()

        assertEquals(
            ReportUiState.Success(testReport),
            viewModel.uiState.value
        )
    }

    @Test
    fun loadReportByInternship_whenReportDoesNotExist_setsEmptyState() = runTest {
        fakeRepository.reportByInternship = null

        viewModel.loadReportByInternship(testReport.internshipId)

        advanceUntilIdle()

        assertEquals(
            ReportUiState.Empty,
            viewModel.uiState.value
        )
    }

    @Test
    fun loadReportByInternship_whenRepositoryThrows_setsErrorState() = runTest {
        fakeRepository.shouldThrowOnGetReportByInternship = true

        viewModel.loadReportByInternship(testReport.internshipId)

        advanceUntilIdle()

        assertEquals(
            ReportUiState.Error("Erro ao carregar relatório por estágio."),
            viewModel.uiState.value
        )
    }

    // --- loadReportsByStudent ---

    @Test
    fun loadReportsByStudent_whenReportsExist_setsSuccessListState() = runTest {
        fakeRepository.reportsByStudent = listOf(testReport)

        viewModel.loadReportsByStudent(testReport.studentId)

        advanceUntilIdle()

        assertEquals(
            ReportUiState.SuccessList(listOf(testReport)),
            viewModel.uiState.value
        )
    }

    @Test
    fun loadReportsByStudent_whenReportsDoNotExist_setsEmptyState() = runTest {
        fakeRepository.reportsByStudent = emptyList()

        viewModel.loadReportsByStudent(testReport.studentId)

        advanceUntilIdle()

        assertEquals(
            ReportUiState.Empty,
            viewModel.uiState.value
        )
    }

    @Test
    fun loadReportsByStudent_whenRepositoryThrows_setsErrorState() = runTest {
        fakeRepository.shouldThrowOnGetReportsByStudent = true

        viewModel.loadReportsByStudent(testReport.studentId)

        advanceUntilIdle()

        assertEquals(
            ReportUiState.Error("Erro ao carregar relatórios por estudante."),
            viewModel.uiState.value
        )
    }

    // --- loadReportsByStatus ---

    @Test
    fun loadReportsByStatus_whenReportsExist_setsSuccessListState() = runTest {
        fakeRepository.reportsByStatus = listOf(testReport)

        viewModel.loadReportsByStatus(ReportStatus.DRAFT)

        advanceUntilIdle()

        assertEquals(
            ReportUiState.SuccessList(listOf(testReport)),
            viewModel.uiState.value
        )
    }

    @Test
    fun loadReportsByStatus_whenReportsDoNotExist_setsEmptyState() = runTest {
        fakeRepository.reportsByStatus = emptyList()

        viewModel.loadReportsByStatus(ReportStatus.DRAFT)

        advanceUntilIdle()

        assertEquals(
            ReportUiState.Empty,
            viewModel.uiState.value
        )
    }

    @Test
    fun loadReportsByStatus_whenRepositoryThrows_setsErrorState() = runTest {
        fakeRepository.shouldThrowOnGetReportsByStatus = true

        viewModel.loadReportsByStatus(ReportStatus.DRAFT)

        advanceUntilIdle()

        assertEquals(
            ReportUiState.Error("Erro ao carregar relatórios por estado."),
            viewModel.uiState.value
        )
    }

    // --- resetState ---

    @Test
    fun resetState_setsIdleState() = runTest {
        fakeRepository.reports = listOf(testReport)

        viewModel.loadReports()

        advanceUntilIdle()

        viewModel.resetState()

        assertEquals(
            ReportUiState.Idle,
            viewModel.uiState.value
        )
    }

    private companion object {
        val testReport = FinalReportModel(
            id = "00000000-0000-0000-0000-000000000001",
            internshipId = "00000000-0000-0000-0000-000000000002",
            studentId = "00000000-0000-0000-0000-000000000003",
            title = "Test Report",
            content = "Test report content",
            fileUrl = null,
            status = ReportStatus.DRAFT,
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z"
        )
    }
}

private class FakeReportRepository : ReportRepositoryInterface {

    var reports: List<FinalReportModel> = emptyList()
    var reportById: FinalReportModel? = null
    var reportByInternship: FinalReportModel? = null
    var reportsByStudent: List<FinalReportModel> = emptyList()
    var reportsByStatus: List<FinalReportModel> = emptyList()

    var shouldThrowOnGetReports: Boolean = false
    var shouldThrowOnGetReportById: Boolean = false
    var shouldThrowOnGetReportByInternship: Boolean = false
    var shouldThrowOnGetReportsByStudent: Boolean = false
    var shouldThrowOnGetReportsByStatus: Boolean = false

    override suspend fun getReports(): List<FinalReportModel> {
        if (shouldThrowOnGetReports) {
            throw IllegalStateException("Erro ao carregar relatórios.")
        }
        return reports
    }

    override suspend fun getReportById(reportId: String): FinalReportModel? {
        if (shouldThrowOnGetReportById) {
            throw IllegalStateException("Erro ao carregar relatório.")
        }
        return reportById
    }

    override suspend fun getReportByInternship(internshipId: String): FinalReportModel? {
        if (shouldThrowOnGetReportByInternship) {
            throw IllegalStateException("Erro ao carregar relatório por estágio.")
        }
        return reportByInternship
    }

    override suspend fun getReportsByStudent(studentId: String): List<FinalReportModel> {
        if (shouldThrowOnGetReportsByStudent) {
            throw IllegalStateException("Erro ao carregar relatórios por estudante.")
        }
        return reportsByStudent
    }

    override suspend fun getReportsByStatus(status: ReportStatus): List<FinalReportModel> {
        if (shouldThrowOnGetReportsByStatus) {
            throw IllegalStateException("Erro ao carregar relatórios por estado.")
        }
        return reportsByStatus
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
