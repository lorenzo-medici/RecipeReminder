package it.loremed.recipereminder.persistence.lista

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import it.loremed.recipereminder.model.lista.ItemLista

@Database(
    entities = [ItemLista::class],
    version = 1,
    exportSchema = false
)
abstract class ListaRoomDatabase : RoomDatabase() {

    abstract fun listaDao(): ListaDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ListaRoomDatabase? = null

        fun getDatabase(
            context: Context,
        ): ListaRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ListaRoomDatabase::class.java,
                    "lista_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

    }
}