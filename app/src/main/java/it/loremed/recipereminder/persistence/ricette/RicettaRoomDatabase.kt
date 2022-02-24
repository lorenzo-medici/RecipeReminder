package it.loremed.recipereminder.persistence.ricette

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.loremed.recipereminder.model.ricette.Ricetta

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
        ): RicettaRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RicettaRoomDatabase::class.java,
                    "ricetta_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

    }
}