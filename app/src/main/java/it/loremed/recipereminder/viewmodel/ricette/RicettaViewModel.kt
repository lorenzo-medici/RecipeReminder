package it.loremed.recipereminder.viewmodel.ricette

import android.util.Log
import androidx.lifecycle.*
import it.loremed.recipereminder.model.ricette.Ricetta
import it.loremed.recipereminder.model.ricette.Tipo
import it.loremed.recipereminder.persistence.ricette.RicettaRepository
import kotlinx.coroutines.launch


class RicettaViewModel(private val repository: RicettaRepository) : ViewModel() {

    var allRecipesFiltered: LiveData<List<Ricetta>>
    private var filter = MutableLiveData<Tipo>(null)

    init {
        allRecipesFiltered = Transformations.switchMap(filter) { filter ->
            repository.ricette.asLiveData()
                .map {
                    it.filter { item ->
                        if (filter != null) item.tipo == filter else true
                    }
                }

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
        filter.postValue( if (newFilter != "") Tipo.fromPlural(newFilter) else null )
        Log.d("FILTERING", "Filter $newFilter applied")
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