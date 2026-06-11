package turmaA.grupoB.LinkStage.data.repository.flags

import turmaA.grupoB.LinkStage.data.remote.RetrofitInstance
import turmaA.grupoB.LinkStage.data.remote.model.Imgs
import turmaA.grupoB.LinkStage.util.DebugLogger

private const val TAG = "FlagsRepository"

class FlagsRepository : FlagsRepositoryInterface {
    override suspend fun getFlag(country: String): Imgs? {
        DebugLogger.d(TAG, "getFlag called with country=$country")
        return try {
            val response = RetrofitInstance.api.getFlagByName(country)
            val img = response.firstOrNull()?.flags
            DebugLogger.d(TAG, "getFlag country=$country responseSize=${response.size} img=${img?.png}")
            img
        } catch (e: Exception) {
            DebugLogger.e(TAG, "getFlag failed for country=$country", e)
            throw e
        }
    }
}