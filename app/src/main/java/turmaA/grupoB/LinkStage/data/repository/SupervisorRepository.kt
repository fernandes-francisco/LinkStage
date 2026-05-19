package turmaA.grupoB.LinkStage.data.repository

import io.github.jan.supabase.postgrest.from
import turmaA.grupoB.LinkStage.data.remote.model.user.SupervisorModel
import turmaA.grupoB.LinkStage.data.remote.model.user.SupervisorSkillModel
import turmaA.grupoB.LinkStage.data.remote.supabase.SupabaseClientProvider

class SupervisorRepository {

    private val supabase = SupabaseClientProvider.client

    suspend fun getSuperVisors(): List<SupervisorModel> {
        return supabase
            .from("supervisors")
            .select()
            .decodeList<SupervisorModel>()
    }

    suspend fun getSupervisorById(supervisorId: String): SupervisorModel? {
        return supabase
            .from("supervisors")
            .select {
                filter {
                    eq("id", supervisorId)
                }
            }
            .decodeList<SupervisorModel>()
            .firstOrNull()
    }

    suspend fun getSuperVisorByUserId(userId: String): SupervisorModel? {
        return supabase
            .from("supervisors")
            .select {
                filter {
                    eq("user_id", userId)
                }
            }
            .decodeList<SupervisorModel>()
            .firstOrNull()
    }

    suspend fun getAvailableSupervisors(): List<SupervisorModel> {
        return supabase
            .from("supervisors")
            .select {
                filter {
                    eq("accepts_new_internships", true)
                }
            }
            .decodeList<SupervisorModel>()
    }

    suspend fun getSupervisorsByDepartment(department:String): List<SupervisorModel> {
        return supabase
            .from("supervisors")
            .select {
                filter {
                    eq("department", department)
                }
            }
            .decodeList<SupervisorModel>()
    }

    suspend fun getSupervisorSkills(supervisorId: String): List<SupervisorSkillModel> {
        return supabase
            .from("supervisor_skills")
            .select {
                filter {
                    eq("supervisor_id", supervisorId)
                }
            }
            .decodeList<SupervisorSkillModel>()
    }

}