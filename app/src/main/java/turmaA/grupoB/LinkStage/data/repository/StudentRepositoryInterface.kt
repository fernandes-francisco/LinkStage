package turmaA.grupoB.LinkStage.data.repository

import turmaA.grupoB.LinkStage.data.remote.model.user.StudentModel

interface StudentRepositoryInterface {
    suspend fun getStudents(): List<StudentModel>
    suspend fun getStudentById(studentId: String): StudentModel?
    suspend fun getStudentByUserId(userId: String): StudentModel?
    suspend fun getStudentByNumber(studentNumber: String): StudentModel?
    suspend fun getStudentsByCourse(course: String): List<StudentModel>
}