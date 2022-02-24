package it.loremed.recipereminder.persistence.ricette

import androidx.annotation.WorkerThread
import it.loremed.recipereminder.model.ricette.Ricetta
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
    suspend fun updateRicetta(ricetta: Ricetta) {
        ricettaDao.update(ricetta)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteRicetta(ricetta: Ricetta) {
        ricettaDao.delete(ricetta)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getByTipo(tipo: String): Flow<List<Ricetta>> {
        return ricettaDao.getByTipo(tipo)
    }
}