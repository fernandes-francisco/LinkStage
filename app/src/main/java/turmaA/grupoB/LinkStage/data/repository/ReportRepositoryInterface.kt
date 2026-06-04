package turmaA.grupoB.LinkStage.data.repository

import turmaA.grupoB.LinkStage.data.remote.model.enums.ReportStatus
import turmaA.grupoB.LinkStage.data.remote.model.report.FinalReportModel

interface ReportRepositoryInterface {
    suspend fun getReports(): List<FinalReportModel>
    suspend fun getReportById(reportId: String): FinalReportModel?
    suspend fun getReportByInternship(internshipId: String): FinalReportModel?
    suspend fun getReportsByStudent(studentId: String): List<FinalReportModel>
    suspend fun getReportsByStatus(status: ReportStatus): List<FinalReportModel>
}
