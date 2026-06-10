package turmaA.grupoB.LinkStage.data.repository

import io.github.jan.supabase.postgrest.from
import turmaA.grupoB.LinkStage.data.remote.model.user.ProfileModel
import turmaA.grupoB.LinkStage.data.remote.supabase.SupabaseClientProvider

class ProfileRepository {

    private val supabase = SupabaseClientProvider.client

    suspend fun getProfiles(): List<ProfileModel> {
        return supabase
            .from("profiles")
            .select()
            .decodeList<ProfileModel>()
    }

    suspend fun getProfileById(userId: String): ProfileModel? {
        return supabase
            .from("profiles")
            .select {
                filter {
                    eq("id", userId)
                }
            }
            .decodeList<ProfileModel>()
            .firstOrNull()
    }

    suspend fun getProfilesByRole(role: String): List<ProfileModel> {
        return supabase
            .from("profiles")
            .select {
                filter {
                    eq("role", role)
                }
            }
            .decodeList<ProfileModel>()
    }
}