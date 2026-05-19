package turmaA.grupoB.LinkStage.data.repository

import io.github.jan.supabase.postgrest.from
import turmaA.grupoB.LinkStage.data.remote.model.instituition.InstituitionModel
import turmaA.grupoB.LinkStage.data.remote.supabase.SupabaseClientProvider

class InstitutionRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun getInstitutions(): List<InstituitionModel>? {
        return supabase
            .from("institutions")
            .select()
            .decodeList<InstituitionModel>()
    }

    suspend fun getInstitutionById(institutionId: String): InstituitionModel? {
        return supabase
            .from("institutions")
            .select {
                filter {
                    eq("id", institutionId)
                }
            }
            .decodeList<InstituitionModel>()
            .firstOrNull()
    }

    suspend fun getInstitutionByUserId(userId: String): InstituitionModel? {
        return supabase
            .from("institutions")
            .select {
                filter {
                    eq("user_id", userId)
                }
            }
            .decodeList<InstituitionModel>()
            .firstOrNull()
    }
}