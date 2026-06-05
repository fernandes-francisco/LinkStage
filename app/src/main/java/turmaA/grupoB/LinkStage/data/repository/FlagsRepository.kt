package turmaA.grupoB.LinkStage.data.repository

import turmaA.grupoB.LinkStage.data.remote.RetrofitInstance
import turmaA.grupoB.LinkStage.data.remote.model.Imgs

class FlagsRepository : FlagsRepositoryInterface {
    override suspend fun getFlag(country: String): Imgs {
        return RetrofitInstance.api.getFlagByName(country)
    }
}