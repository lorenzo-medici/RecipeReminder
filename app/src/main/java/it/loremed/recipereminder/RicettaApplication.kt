package it.loremed.recipereminder

import android.app.Application
import it.loremed.recipereminder.persistence.ricette.RicettaRepository
import it.loremed.recipereminder.persistence.ricette.RicettaRoomDatabase

class RicettaApplication : Application() {

    private val ricettaDatabase by lazy { RicettaRoomDatabase.getDatabase(this) }
    val ricettaRepository by lazy { RicettaRepository(ricettaDatabase.ricettaDao()) }
}