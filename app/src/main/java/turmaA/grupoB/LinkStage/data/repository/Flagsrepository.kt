package turmaA.grupoB.LinkStage.data.repository

import turmaA.grupoB.LinkStage.data.remote.RetrofitInstance
import turmaA.grupoB.LinkStage.data.remote.model.Imgs

class Flagsrepository{
    suspend fun getFlag(country: String): Imgs {
        return RetrofitInstance.api.getFlagByName(country)
    }
}