package it.loremed.recipereminder.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.loremed.recipereminder.model.Ricetta

@Database(
    entities = [Ricetta::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ricettaDao(): RicettaDao
}