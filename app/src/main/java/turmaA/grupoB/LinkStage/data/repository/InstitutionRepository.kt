package turmaA.grupoB.LinkStage.data.repository

import io.github.jan.supabase.postgrest.from
import turmaA.grupoB.LinkStage.data.remote.model.institution.InstitutionModel
import turmaA.grupoB.LinkStage.data.remote.supabase.SupabaseClientProvider

class InstitutionRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun getInstitutions(): List<InstitutionModel>? {
        return supabase
            .from("institutions")
            .select()
            .decodeList<InstitutionModel>()
    }

    suspend fun getInstitutionById(institutionId: String): InstitutionModel? {
        return supabase
            .from("institutions")
            .select {
                filter {
                    eq("id", institutionId)
                }
            }
            .decodeList<InstitutionModel>()
            .firstOrNull()
    }

    suspend fun getInstitutionByUserId(userId: String): InstitutionModel? {
        return supabase
            .from("institutions")
            .select {
                filter {
                    eq("user_id", userId)
                }
            }
            .decodeList<InstitutionModel>()
            .firstOrNull()
    }
}