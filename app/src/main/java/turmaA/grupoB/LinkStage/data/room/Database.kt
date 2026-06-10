package turmaA.grupoB.LinkStage.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.Locale


@Database(
    entities = [ActivityLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AtDatabase: RoomDatabase(){
    abstract fun atividadeDAO(): AtividadeDAO
    
    companion object{
        @Volatile
        private var INSTANCE: AtDatabase? = null
        
        fun getDatabase(context: Context): AtDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AtDatabase::class.java,
                    "atividades_aluno"
                ).build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}