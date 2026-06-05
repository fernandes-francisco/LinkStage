package turmaA.grupoB.LinkStage.data.repository.report

import io.github.jan.supabase.postgrest.from
import turmaA.grupoB.LinkStage.data.remote.model.enums.ReportStatus
import turmaA.grupoB.LinkStage.data.remote.model.report.CreateFinalReportInput
import turmaA.grupoB.LinkStage.data.remote.model.report.FinalReportModel
import turmaA.grupoB.LinkStage.data.remote.model.report.UpdateFinalReportInput
import turmaA.grupoB.LinkStage.data.remote.supabase.SupabaseClientProvider
import java.time.Instant

class ReportRepository : ReportRepositoryInterface {

    private val supabase = SupabaseClientProvider.client

    override suspend fun getReports(): List<FinalReportModel> {
        return supabase
            .from("final_reports")
            .select()
            .decodeList<FinalReportModel>()
    }

    override suspend fun getReportById(reportId: String): FinalReportModel? {
        return supabase
            .from("final_reports")
            .select {
                filter {
                    eq("id", reportId)
                }
            }
            .decodeList<FinalReportModel>()
            .firstOrNull()
    }

    override suspend fun getReportByInternship(internshipId: String): FinalReportModel? {
        return supabase
            .from("final_reports")
            .select {
                filter {
                    eq("internship_id", internshipId)
                }
            }
            .decodeList<FinalReportModel>()
            .firstOrNull()
    }

    override suspend fun getReportsByStudent(studentId: String): List<FinalReportModel> {
        return supabase
            .from("final_reports")
            .select {
                filter {
                    eq("student_id", studentId)
                }
            }
            .decodeList<FinalReportModel>()
    }

    override suspend fun getReportsByStatus(status: ReportStatus): List<FinalReportModel> {
        return supabase
            .from("final_reports")
            .select {
                filter {
                    eq("status", status.name)
                }
            }
            .decodeList<FinalReportModel>()
    }

    override suspend fun createReport(input: CreateFinalReportInput): FinalReportModel {
        return supabase
            .from("final_reports")
            .insert(input) {
                select()
            }
            .decodeSingle<FinalReportModel>()
    }

    override suspend fun updateReport(
        reportId: String,
        input: UpdateFinalReportInput
    ): FinalReportModel {
        return supabase
            .from("final_reports")
            .update(input) {
                filter {
                    eq("id", reportId)
                }
                select()
            }
            .decodeSingle<FinalReportModel>()
    }

    override suspend fun submitReport(reportId: String): FinalReportModel {
        val now = Instant.now().toString()

        val input = UpdateFinalReportInput(
            status = ReportStatus.SUBMITTED,
            updatedAt = now
        )

        return updateReport(
            reportId = reportId,
            input = input
        )
    }
}