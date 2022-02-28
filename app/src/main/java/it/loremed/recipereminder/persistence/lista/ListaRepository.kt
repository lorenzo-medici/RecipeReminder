package it.loremed.recipereminder.persistence.lista

import androidx.annotation.WorkerThread
import it.loremed.recipereminder.model.lista.ItemLista
import kotlinx.coroutines.flow.Flow


class ListaRepository(private val listaDao: ListaDao) {

    var items: Flow<List<ItemLista>> = listaDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addItemLista(ricetta: ItemLista) {
        listaDao.insert(ricetta)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun editItemLista(ricetta: ItemLista) {
        listaDao.update(ricetta)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteItemLista(ricetta: ItemLista) {
        listaDao.delete(ricetta)
    }
}