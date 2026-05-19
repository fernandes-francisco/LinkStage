package turmaA.grupoB.LinkStage.data.repository

import io.github.jan.supabase.postgrest.from
import turmaA.grupoB.LinkStage.data.remote.model.user.StudentModel
import turmaA.grupoB.LinkStage.data.remote.supabase.SupabaseClientProvider

class StudentRepository {

    private val supabase = SupabaseClientProvider.client

    suspend fun getStudents(): List<StudentModel> {
        return supabase
            .from("students")
            .select()
            .decodeList<StudentModel>()
    }

    suspend fun getStudentById(studentId: String): StudentModel? {
        return supabase
            .from("students")
            .select {
                filter {
                    eq("id", studentId)
                }
            }
            .decodeList<StudentModel>()
            .firstOrNull()
    }

    suspend fun getStudentByUserId(userId: String): StudentModel? {
        return supabase
            .from("students")
            .select {
                filter {
                    eq("user_id", userId)
                }
            }
            .decodeList<StudentModel>()
            .firstOrNull()
    }

    suspend fun getStudentByNumber(studentNumber: String): StudentModel? {
        return supabase
            .from("students")
            .select {
                filter {
                    eq("student_number", studentNumber)
                }
            }
            .decodeList<StudentModel>()
            .firstOrNull()
    }

    suspend fun getStudentByCourse(course: String): List<StudentModel> {
        return supabase
            .from("students")
            .select {
                filter {
                    eq("course", course)
                }
            }
            .decodeList<StudentModel>()
    }
}