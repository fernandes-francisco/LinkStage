package turmaA.grupoB.LinkStage.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AtividadeDAO{
    @Query("SELECT * FROM atividades_aluno ORDER BY createdAt ASC")
    fun getAtividades(): Flow<List<ActivityLogEntity>>

    @Insert
    suspend fun insert(atividade: ActivityLogEntity)

    @Delete
    suspend fun delete(atividade: ActivityLogEntity)

    @Update
    suspend fun update(atividade: ActivityLogEntity)
}
