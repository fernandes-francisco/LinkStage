package turmaA.grupoB.LinkStage.data.repository

import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.JsonObject
import turmaA.grupoB.LinkStage.data.remote.supabase.SupabaseClientProvider

public class InvalidTableRepository {

    private val supabase = SupabaseClientProvider.client

    suspend fun queryInvalidTable(): List<JsonObject> {
        return supabase
            .from("table_that_does_not_exist")
            .select()
            .decodeList<JsonObject>()
    }
}