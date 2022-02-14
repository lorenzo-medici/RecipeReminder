package it.loremed.recipereminder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.loremed.recipereminder.model.Ricetta
import it.loremed.recipereminder.persistence.RicettaRepository
import java.lang.IllegalArgumentException
import kotlinx.coroutines.launch
import androidx.lifecycle.*


class RicettaViewModel(private val repository: RicettaRepository) : ViewModel() {

    val allRecipes: LiveData<List<Ricetta>> = repository.ricette.asLiveData()

    fun insert(ricetta: Ricetta) = viewModelScope.launch {
        repository.addRicetta(ricetta)
    }

    fun delete(ricetta: Ricetta) = viewModelScope.launch {
        repository.deleteRicetta(ricetta)
    }

    class RicettaViewModelFactory(private val repository: RicettaRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RicettaViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RicettaViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}