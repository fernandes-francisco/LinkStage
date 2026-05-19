package turmaA.grupoB.LinkStage.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertThrows

@RunWith(AndroidJUnit4::class)
class RepositoryIntegrationTest {

    private val profileRepository = ProfileRepository()
    private val offerRepository = OfferRepository()
    private val institutionRepository = InstitutionRepository()
    private val applicationRepository = ApplicationRepository()
    private val internshipRepository = InternshipRepository()
    private val evaluationRepository = EvaluationRepository()
    private val communicationRepository = CommunicationRepository()
    private val invalidTableRepository = InvalidTableRepository()

    @Test
    fun repository_throwsExceptionWhenTableDoesNotExist() {
        assertThrows(PostgrestRestException::class.java) {
            runBlocking {
                invalidTableRepository.queryInvalidTable()
            }
        }
    }

    @Test
    fun profileRepository_canFetchProfiles() = runBlocking {
        val profiles = profileRepository.getProfiles()

        assertNotNull(
            "ProfileRepository deveria devolver uma lista, mesmo que vazia.",
            profiles
        )
    }

    @Test
    fun offerRepository_canFetchPublishedOffers() = runBlocking {
        val offers = offerRepository.getPublishedOffers()

        assertNotNull(
            "OfferRepository deveria devolver uma lista, mesmo que vazia.",
            offers
        )
    }

    @Test
    fun institutionRepository_canFetchInstitutions() = runBlocking {
        val institutions = institutionRepository.getInstitutions()

        assertNotNull(
            "InstitutionRepository deveria devolver uma lista, mesmo que vazia.",
            institutions
        )
    }

    @Test
    fun applicationRepository_canFetchApplicationsByStudent() = runBlocking {
        val applications = applicationRepository.getApplicationsByStudent(
            studentId = "00000000-0000-0000-0000-000000000000"
        )

        assertNotNull(
            "ApplicationRepository deveria devolver uma lista, mesmo que vazia.",
            applications
        )
    }

    @Test
    fun internshipRepository_canFetchInternshipsByStudent() = runBlocking {
        val internships = internshipRepository.getInternshipsByStudent(
            studentId = "00000000-0000-0000-0000-000000000000"
        )

        assertNotNull(
            "InternshipRepository deveria devolver uma lista, mesmo que vazia.",
            internships
        )
    }

    @Test
    fun evaluationRepository_canFetchEvaluations() = runBlocking {
        val evaluations = evaluationRepository.getEvaluations()

        assertNotNull(
            "EvaluationRepository deveria devolver uma lista, mesmo que vazia.",
            evaluations
        )
    }

    @Test
    fun communicationRepository_canFetchNotificationsForUnknownUser() = runBlocking {
        val notifications = communicationRepository.getNotificationsByUser(
            userId = "00000000-0000-0000-0000-000000000000"
        )

        assertNotNull(
            "CommunicationRepository deveria devolver uma lista, mesmo que vazia.",
            notifications
        )
    }
}