package it.loremed.recipereminder.viewmodel

import androidx.lifecycle.*
import it.loremed.recipereminder.model.Ricetta
import it.loremed.recipereminder.persistence.RicettaRepository
import kotlinx.coroutines.launch


class RicettaViewModel(private val repository: RicettaRepository) : ViewModel() {

    val allRecipes: LiveData<List<Ricetta>> = repository.ricette.asLiveData()

    fun insert(ricetta: Ricetta) = viewModelScope.launch {
        repository.addRicetta(ricetta)
    }

    fun delete(ricetta: Ricetta) = viewModelScope.launch {
        repository.deleteRicetta(ricetta)
    }

    fun update(ricetta: Ricetta) = viewModelScope.launch {
        repository.updateRicetta(ricetta)
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