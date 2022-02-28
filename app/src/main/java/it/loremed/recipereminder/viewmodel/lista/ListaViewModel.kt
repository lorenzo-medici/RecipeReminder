package it.loremed.recipereminder.viewmodel.lista

import androidx.lifecycle.*
import it.loremed.recipereminder.model.lista.ItemLista
import it.loremed.recipereminder.persistence.lista.ListaRepository
import kotlinx.coroutines.launch


class ListaViewModel(private val repository: ListaRepository) : ViewModel() {

    var alItems: LiveData<List<ItemLista>> = repository.items.asLiveData()

    fun insert(item: ItemLista) = viewModelScope.launch {
        repository.addItemLista(item)
    }

    fun delete(item: ItemLista) = viewModelScope.launch {
        repository.deleteItemLista(item)
    }

    fun update(item: ItemLista) = viewModelScope.launch {
        repository.editItemLista(item)
    }

    class ItemListaViewModelFactory(private val repository: ListaRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListaViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ListaViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}