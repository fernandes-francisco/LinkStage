package turmaA.grupoB.LinkStage.data.repository

import android.R.id.input
import io.github.jan.supabase.postgrest.from
import io.ktor.client.utils.EmptyContent.status
import turmaA.grupoB.LinkStage.data.remote.model.application.ApplicationModel
import turmaA.grupoB.LinkStage.data.remote.model.application.CreateApplicationInput
import turmaA.grupoB.LinkStage.data.remote.model.application.UpdateApplicationDecisionInput
import turmaA.grupoB.LinkStage.data.remote.model.enums.ApplicationStatus
import turmaA.grupoB.LinkStage.data.remote.supabase.SupabaseClientProvider

class ApplicationRepository {

    private val supabase = SupabaseClientProvider.client

    suspend fun getApplicationById(applicationId: String): ApplicationModel? {
        return supabase
            .from("applications")
            .select {
                filter {
                    eq("id", applicationId)
                }
            }
            .decodeList<ApplicationModel>()
            .firstOrNull()
    }

    suspend fun getApplicationsByOffer(offerId: String): List<ApplicationModel> {
        return supabase
            .from("applications")
            .select {
                filter {
                    eq("offer_id", offerId)
                }
            }
            .decodeList<ApplicationModel>()
    }

    suspend fun getApplicationsByStudent(studentId: String): List<ApplicationModel> {
        return supabase
            .from("applications")
            .select {
                filter {
                    eq("student_id", studentId)
                }
            }
            .decodeList<ApplicationModel>()
    }

    suspend fun getApplicationsByStatus(status: ApplicationStatus): List<ApplicationModel> {
        return supabase
            .from("applications")
            .select {
                filter {
                    eq("status", status.name)
                }
            }
            .decodeList<ApplicationModel>()
    }

    suspend fun createApplication(input: CreateApplicationInput): ApplicationModel {
        return supabase
            .from("applications")
            .insert(input) {
                select()
            }
            .decodeSingle<ApplicationModel>()
    }

    suspend fun updateApplicationDecision(
        applicationId: String,
        input: UpdateApplicationDecisionInput
    ): ApplicationModel {
        return supabase
            .from("applications")
            .update(input) {
                select()
                filter {
                    eq("id", applicationId)
                }
            }
            .decodeSingle<ApplicationModel>()
    }

    suspend fun acceptApplication(
        applicationId: String,
        decisionAt: String
    ): ApplicationModel {
        return updateApplicationDecision(
            applicationId=applicationId,
            input=UpdateApplicationDecisionInput(
                status=ApplicationStatus.ACCEPTED,
                rejectionReason=null,
                decisionAt=decisionAt
            )
        )
    }

    suspend fun rejectApplication(
        applicationId: String,
        rejectionReason: String,
        decisionAt: String
    ): ApplicationModel {
        return updateApplicationDecision(
            applicationId=applicationId,
            input=UpdateApplicationDecisionInput(
                status=ApplicationStatus.REJECTED,
                rejectionReason=rejectionReason,
                decisionAt=decisionAt
            )
        )
    }
}