package it.loremed.recipereminder.viewmodel

import android.util.Log
import androidx.lifecycle.*
import it.loremed.recipereminder.model.Ricetta
import it.loremed.recipereminder.persistence.RicettaRepository
import kotlinx.coroutines.launch


class RicettaViewModel(private val repository: RicettaRepository) : ViewModel() {

    var allRecipesFiltered: LiveData<List<Ricetta>>
    private var filter = MutableLiveData("%")

    init {
        allRecipesFiltered = Transformations.switchMap(filter) { filter ->
            repository.getByTipo(filter).asLiveData()
        }
    }

    fun insert(ricetta: Ricetta) = viewModelScope.launch {
        repository.addRicetta(ricetta)
    }

    fun delete(ricetta: Ricetta) = viewModelScope.launch {
        repository.deleteRicetta(ricetta)
    }

    fun update(ricetta: Ricetta) = viewModelScope.launch {
        repository.updateRicetta(ricetta)
    }

    fun setFilter(newFilter: String) {
        val f = when {
            newFilter.isEmpty() -> "%"
            else -> "%$newFilter%"
        }

        Log.d("FILTERING", "Filter $f applied")
        filter.postValue(f) // apply the filter
    }

    class RicettaViewModelFactory(private val repository: RicettaRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RicettaViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RicettaViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}