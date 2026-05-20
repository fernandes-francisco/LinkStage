package turmaA.grupoB.LinkStage.data.repository

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import turmaA.grupoB.LinkStage.data.remote.model.auth.SignInInput
import turmaA.grupoB.LinkStage.data.remote.model.auth.SignUpInput
import turmaA.grupoB.LinkStage.data.remote.model.user.CreateProfileInput
import turmaA.grupoB.LinkStage.data.remote.model.user.ProfileModel
import turmaA.grupoB.LinkStage.data.remote.model.user.UpdateProfileInput
import turmaA.grupoB.LinkStage.data.remote.supabase.SupabaseClientProvider
import java.time.Instant
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

class AuthRepository {

    private val supabase = SupabaseClientProvider.client

    val sessionStatus: Flow<SessionStatus> = supabase.auth.sessionStatus

    suspend fun signUp(input: SignUpInput): ProfileModel {
        require(input.rgpdConsent) {
            "É necessário consentir o tratamento de dados pessoais."
        }

        val user = supabase.auth.signUpWith(Email) {
            email = input.email
            password = input.password
        } ?: throw IllegalStateException("Não foi possível criar o utilizador")

        val userId = user.id

        val profileInput = CreateProfileInput(
            id = userId,
            name = input.name,
            email = input.email,
            phone = input.phone,
            role = input.role,
            rgpdConsent = input.rgpdConsent,
            rgpdConsentAt = Instant.now().toString()
        )

        return createProfile(profileInput)
    }

    suspend fun signIn(input: SignInInput) {
        supabase.auth.signInWith(Email) {
            email = input.email
            password = input.password
        }
    }

    suspend fun signOut() {
        supabase.auth.signOut()
    }

    fun getCurrentUserId(): String? {
        return supabase.auth.currentSessionOrNull()?.user?.id
    }

    fun isUserLoggedIn(): Boolean {
        return supabase.auth.currentSessionOrNull() != null
    }

    suspend fun getCurrentUserProfile(): ProfileModel? {
        val userId = getCurrentUserId() ?: return null

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

    suspend fun createProfile(input: CreateProfileInput): ProfileModel {
        return supabase
            .from("profiles")
            .insert(input) {
                select()
            }
            .decodeSingle<ProfileModel>()
    }

    suspend fun updateProfile(
        userId: String,
        input: UpdateProfileInput
    ): ProfileModel {
        val updateData = input.toJsonObject()

        require(updateData.isNotEmpty()) {
            "UpdateProfileInput não contém campos para atualizar."
        }

        return supabase
            .from("profiles")
            .update(updateData) {
                select()
                filter {
                    eq("id", userId)
                }
            }
            .decodeSingle<ProfileModel>()
    }

    private fun UpdateProfileInput.toJsonObject(): JsonObject {
        return buildJsonObject {
            name?.let { put("name", JsonPrimitive(it)) }
            phone?.let { put("phone", JsonPrimitive(it)) }
            photoUrl?.let { put("photo_url", JsonPrimitive(it)) }
        }
    }
}