package it.loremed.recipereminder.persistence

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import it.loremed.recipereminder.model.Ricetta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Ricetta::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class RicettaRoomDatabase : RoomDatabase() {

    abstract fun ricettaDao(): RicettaDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: RicettaRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): RicettaRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RicettaRoomDatabase::class.java,
                    "ricetta_database"
                )
                .addCallback(RicettaDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

    }

    private class RicettaDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.ricettaDao())
                }
            }
        }

        suspend fun populateDatabase(ricettaDao: RicettaDao) {
            // Do nothing
        }
    }

}