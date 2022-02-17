package it.loremed.recipereminder

import android.app.Application
import it.loremed.recipereminder.persistence.RicettaRepository
import it.loremed.recipereminder.persistence.RicettaRoomDatabase

class RicettaApplication : Application() {

    private val database by lazy { RicettaRoomDatabase.getDatabase(this) }
    val repository by lazy { RicettaRepository(database.ricettaDao()) }
}