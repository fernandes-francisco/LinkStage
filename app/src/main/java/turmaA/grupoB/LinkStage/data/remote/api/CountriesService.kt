package turmaA.grupoB.LinkStage.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path
import turmaA.grupoB.LinkStage.data.remote.model.Imgs

interface CountriesService{
    @GET("v3.1/{country}?fields=flags")
    suspend fun getFlagByName(@Path("country") country: String): Imgs
}