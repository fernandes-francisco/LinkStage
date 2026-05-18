package turmaA.grupoB.LinkStage.data.remote.supabase

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.jan.supabase.postgrest.from
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonObject
import org.junit.Test
import org.junit.runner.RunWith
import turmaA.grupoB.LinkStage.BuildConfig

@RunWith(AndroidJUnit4::class)
class SupabaseConnectionTest {

    @Test
    fun supabaseClinet_isConfigured() {
        assertFalse(
            "SUPABASE_URL não está configurado no local.properties.",
            BuildConfig.SUPABASE_URL.isBlank()
        )

        assertFalse(
            "SUPABASE_ANON_KEY não está configurada no local.properties.",
            BuildConfig.SUPABASE_ANON_KEY.isBlank()
        )

        assertNotNull(
            "SupabaseClientProvider.client não foi inicializado.",
            SupabaseClientProvider.client
        )
    }

    @Test
    fun supabaseClient_canQueryProfilesTable() = runBlocking {
        val response = SupabaseClientProvider.client
            .from("profiles")
            .select()
            .decodeList<JsonObject>()

        assertNotNull(response)
    }
}