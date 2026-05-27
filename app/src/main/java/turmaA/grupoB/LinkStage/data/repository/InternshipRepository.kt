package turmaA.grupoB.LinkStage.data.repository

import io.github.jan.supabase.postgrest.from
import turmaA.grupoB.LinkStage.data.remote.model.enums.InternshipStatus
import turmaA.grupoB.LinkStage.data.remote.model.internship.ActivityLogModel
import turmaA.grupoB.LinkStage.data.remote.model.internship.AssignSupervisorInput
import turmaA.grupoB.LinkStage.data.remote.model.internship.CreateActivityLogInput
import turmaA.grupoB.LinkStage.data.remote.model.internship.InternshipModel
import turmaA.grupoB.LinkStage.data.remote.supabase.SupabaseClientProvider

class InternshipRepository {

    private val supabase = SupabaseClientProvider.client

    suspend fun getInterships(): List<InternshipModel> {
        return supabase
            .from("internships")
            .select()
            .decodeList<InternshipModel>()
    }

    suspend fun getInternshipById(internshipId: String): InternshipModel? {
        return supabase
            .from("internships")
            .select {
                filter {
                    eq("id", internshipId)
                }
            }
            .decodeList<InternshipModel>()
            .firstOrNull()
    }

    suspend fun getInternshipsByStudent(studentId: String): List<InternshipModel> {
        return supabase
            .from("internships")
            .select {
                filter {
                    eq("student_id", studentId)
                }
            }
            .decodeList<InternshipModel>()
    }

    suspend fun getInternshipsByInstitution(institutionId: String): List<InternshipModel> {
        return supabase
            .from("internships")
            .select {
                filter {
                    eq("institution_id", institutionId)
                }
            }
            .decodeList<InternshipModel>()
    }

    suspend fun getInternshipsByStatus(status: InternshipStatus): List<InternshipModel> {
        return supabase
            .from("interships")
            .select {
                filter {
                    eq("status", status.name)
                }
            }
            .decodeList<InternshipModel>()
    }

    suspend fun assingSupervisor(
        internshipId: String,
        input: AssignSupervisorInput
    ): InternshipModel {
        return supabase
            .from("internships")
            .update(input) {
                select()
                filter {
                    eq("id", internshipId)
                }
            }
            .decodeSingle<InternshipModel>()
    }

    suspend fun getActivityLogsByInternship(internshipId: String): List<ActivityLogModel> {
        return supabase
            .from("activity_logs")
            .select {
                filter {
                    eq("internship_id", internshipId)
                }
            }
            .decodeList<ActivityLogModel>()
    }

    suspend fun createActivityLog(input: CreateActivityLogInput): ActivityLogModel {
        return supabase
            .from("activity_logs")
            .insert(input) {
                select()
            }
            .decodeSingle<ActivityLogModel>()
    }

}