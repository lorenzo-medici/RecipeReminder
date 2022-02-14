package it.loremed.recipereminder.persistence

import androidx.annotation.WorkerThread
import it.loremed.recipereminder.model.Ricetta
import kotlinx.coroutines.flow.Flow


class RicettaRepository(private val ricettaDao: RicettaDao) {

    var ricette: Flow<List<Ricetta>> = ricettaDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addRicetta(ricetta: Ricetta) {
        ricettaDao.insert(ricetta)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteRicetta(ricetta: Ricetta) {
        ricettaDao.delete(ricetta)
    }
}