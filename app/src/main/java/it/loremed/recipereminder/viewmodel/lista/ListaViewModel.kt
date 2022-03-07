package it.loremed.recipereminder.viewmodel.lista

import androidx.lifecycle.*
import it.loremed.recipereminder.model.lista.ItemLista
import it.loremed.recipereminder.persistence.lista.ListaRepository
import kotlinx.coroutines.launch


class ListaViewModel(private val repository: ListaRepository) : ViewModel() {

    var allItems: LiveData<List<ItemLista>> = repository.items.asLiveData()

    fun insert(item: ItemLista) = viewModelScope.launch {
        if (allItems.value?.contains(item) == false) {
            repository.addItemLista(item)
        }
    }

    fun delete(item: ItemLista) = viewModelScope.launch {
        repository.deleteItemLista(item)
    }

    fun update(item: ItemLista) = viewModelScope.launch {
        repository.editItemLista(item)
    }

    class ListaViewModelFactory(private val repository: ListaRepository) :
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