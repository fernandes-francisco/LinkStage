package turmaA.grupoB.LinkStage.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import turmaA.grupoB.LinkStage.data.remote.model.auth.SignInInput
import turmaA.grupoB.LinkStage.data.remote.model.auth.SignUpInput
import turmaA.grupoB.LinkStage.data.remote.model.enums.UserRole

@RunWith(AndroidJUnit4::class)
class AuthRepositoryIntegrationTest {

    private val authRepository = AuthRepository()

    @Test
    fun signUp_withoutRgpdConsent_throwsIllegalArgumentException() {
        val input = SignUpInput(
            name = "Test User",
            email = "test.user@example.com",
            password = "password123",
            phone = null,
            role = UserRole.STUDENT,
            rgpdConsent = false
        )

        assertThrows(
            "O registo sem consentimento RGPD deverial lançar IllegalArgumentException.",
            IllegalArgumentException::class.java
        ) {
            runBlocking {
                authRepository.signUp(input)
            }
        }
    }

    @Test
    fun signIn_withInvalidCredentials_throwsException(){
        val input = SignInInput(
            email = "invalid.user@example.com",
            password = "wrong-password"
        )

        assertThrows(
            "O login com credenciais inválidas deveria lançar uma exceção.",
            Exception::class.java
        ) {
            runBlocking {
                authRepository.signIn(input)
            }
        }
    }

    @Test
    fun signOut_doesNotCrash() = runBlocking {
        authRepository.signOut()

        assertTrue(
            "O logout deve executar sem crash.",
            true
        )
    }

    @Test
    fun currentUserId_canBeCheckedWithoutCrash() {
        val currentUserId = authRepository.getCurrentUserId()

        assertTrue(
            "O utilizador atual pode ser null ou um id válido",
            currentUserId == null || currentUserId.isNotBlank()
        )
    }

}