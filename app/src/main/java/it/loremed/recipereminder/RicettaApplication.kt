package it.loremed.recipereminder

import android.app.Application
import it.loremed.recipereminder.persistence.RicettaRepository
import it.loremed.recipereminder.persistence.RicettaRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RicettaApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { RicettaRoomDatabase.getDatabase(this) }
    val repository by lazy { RicettaRepository(database.ricettaDao())}
}