package turmaA.grupoB.LinkStage.data.repository

import turmaA.grupoB.LinkStage.data.remote.model.user.ProfileModel

interface ProfileRepositoryInterface {
    suspend fun getProfiles(): List<ProfileModel>
    suspend fun getProfileById(userId: String): ProfileModel?
    suspend fun getProfilesByRole(role: String): List<ProfileModel>
}