package turmaA.grupoB.LinkStage.data.repository

import turmaA.grupoB.LinkStage.data.remote.model.institution.InstitutionModel

interface InstitutionRepositoryInterface {
    suspend fun getInstitutions(): List<InstitutionModel>
    suspend fun getInstitutionById(institutionId: String): InstitutionModel?
    suspend fun getInstitutionByUserId(userId: String): InstitutionModel?
}