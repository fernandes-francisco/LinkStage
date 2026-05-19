package turmaA.grupoB.LinkStage.data.repository

import io.github.jan.supabase.postgrest.from
import turmaA.grupoB.LinkStage.data.remote.model.enums.ReportStatus
import turmaA.grupoB.LinkStage.data.remote.model.report.FinalReportModel
import turmaA.grupoB.LinkStage.data.remote.supabase.SupabaseClientProvider

class ReportRepository {

    private val supabase = SupabaseClientProvider.client

    suspend fun getReports(): List<FinalReportModel> {
        return supabase
            .from("final_reports")
            .select()
            .decodeList<FinalReportModel>()
    }

    suspend fun getReportById(reportId: String): FinalReportModel? {
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

    suspend fun getReportByInternship(internshipId: String): FinalReportModel? {
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

    suspend fun getReportsByStudent(studentId: String): List<FinalReportModel> {
        return supabase
            .from("final:reports")
            .select {
                filter {
                    eq("student_id", studentId)
                }
            }
            .decodeList<FinalReportModel>()
    }

    suspend fun getReportsByStatus(status: ReportStatus): List<FinalReportModel> {
        return supabase
            .from("final_reports")
            .select {
                filter {
                    eq("status", status.name)
                }
            }
            .decodeList<FinalReportModel>()
    }
}