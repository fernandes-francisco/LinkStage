package turmaA.grupoB.LinkStage.data.repository

import turmaA.grupoB.LinkStage.data.remote.model.enums.InternshipStatus
import turmaA.grupoB.LinkStage.data.remote.model.internship.ActivityLogModel
import turmaA.grupoB.LinkStage.data.remote.model.internship.AssignSupervisorInput
import turmaA.grupoB.LinkStage.data.remote.model.internship.CreateActivityLogInput
import turmaA.grupoB.LinkStage.data.remote.model.internship.InternshipModel

interface InternshipRepositoryInterface {
    suspend fun getInterships(): List<InternshipModel>
    suspend fun getInternshipById(internshipId: String): InternshipModel?
    suspend fun getInternshipsByStudent(studentId: String): List<InternshipModel>
    suspend fun getInternshipsByInstitution(institutionId: String): List<InternshipModel>
    suspend fun getInternshipsByStatus(status: InternshipStatus): List<InternshipModel>
    suspend fun assingSupervisor(internshipId: String, input: AssignSupervisorInput): InternshipModel
    suspend fun getActivityLogsByInternship(internshipId: String): List<ActivityLogModel>
    suspend fun createActivityLog(input: CreateActivityLogInput): ActivityLogModel
}