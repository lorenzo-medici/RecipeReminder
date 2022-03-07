package it.loremed.recipereminder.persistence.lista

import androidx.annotation.WorkerThread
import it.loremed.recipereminder.model.lista.ItemLista
import kotlinx.coroutines.flow.Flow


class ListaRepository(private val listaDao: ListaDao) {

    var items: Flow<List<ItemLista>> = listaDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addItemLista(itemLista: ItemLista) {
        listaDao.insert(itemLista)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun editItemLista(itemLista: ItemLista) {
        listaDao.update(itemLista)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteItemLista(itemLista: ItemLista) {
        listaDao.delete(itemLista)
    }

}