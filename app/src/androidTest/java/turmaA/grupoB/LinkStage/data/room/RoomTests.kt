package turmaA.grupoB.LinkStage.data.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AtividadeDAOTest {

    private lateinit var database: AtDatabase
    private lateinit var dao: AtividadeDAO

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            AtDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        dao = database.atividadeDAO()
    }

    @After
    @Throws(IOException::class)
    fun close() {
        database.close()
    }

    @Test
    fun insertAndGetActivities() = runBlocking {
        val activity = ActivityLogEntity(
            id = "1",
            internshipId = "internship1",
            studentId = "student1",
            description = "Description",
            activityDate = "2023-01-01",
            hours = 8.0,
            type = "Type",
            location = "Location",
            createdAt = "2023-01-01T10:00:00"
        )

        dao.insert(activity)
        val result = dao.getAtividades().first()

        assertEquals(1, result.size)
        assertEquals(activity, result[0])
    }

    @Test
    fun updateActivity() = runBlocking {

        val activity = ActivityLogEntity(
            id = "1",
            internshipId = "internship1",
            studentId = "student1",
            description = "Description",
            activityDate = "2023-01-01",
            hours = 8.0,
            type = "Type",
            location = "Location",
            createdAt = "2023-01-01T10:00:00"
        )
        dao.insert(activity)

        val updatedActivity = activity.copy(description = "Updated Description")
        dao.update(updatedActivity)

        val result = dao.getAtividades().first()
        assertEquals(1, result.size)
        assertEquals(updatedActivity, result[0])
    }

    @Test
    fun deleteActivity() = runBlocking {
        val activity = ActivityLogEntity(
            id = "1",
            internshipId = "internship1",
            studentId = "student1",
            description = "Description",
            activityDate = "2023-01-01",
            hours = 8.0,
            type = "Type",
            location = "Location",
            createdAt = "2023-01-01T10:00:00"
        )
        dao.insert(activity)

        dao.delete(activity)
        val result = dao.getAtividades().first()
        assertTrue(result.isEmpty())
    }
}