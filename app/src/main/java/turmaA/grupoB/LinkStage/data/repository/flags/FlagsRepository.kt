package turmaA.grupoB.LinkStage.data.repository.flags

import turmaA.grupoB.LinkStage.data.remote.RetrofitInstance
import turmaA.grupoB.LinkStage.data.remote.model.Imgs

class FlagsRepository : FlagsRepositoryInterface {
    private val directFlagUrls = mapOf(
        "portugal" to "https://flagcdn.com/w320/pt.png",
        "gb" to "https://flagcdn.com/w320/gb.png",
    )

    override suspend fun getFlag(country: String): Imgs? {
        val normalizedCountry = country.lowercase()
        directFlagUrls[normalizedCountry]?.let { return Imgs(png = it) }

        return RetrofitInstance.api.getFlagByName(normalizedCountry).firstOrNull()?.flags
    }
}