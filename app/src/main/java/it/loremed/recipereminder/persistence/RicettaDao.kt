package it.loremed.recipereminder.persistence

import androidx.room.*
import it.loremed.recipereminder.model.Ricetta
import it.loremed.recipereminder.model.Tipo

@Dao
interface RicettaDao {
    @Query("SELECT * FROM ricetta")
    fun getAll(): List<Ricetta>

    @Insert
    fun insertAll(vararg ricettas: Ricetta)

    @Delete
    fun delete(ricetta: Ricetta)

    @Query("SELECT * FROM ricetta WHERE tipo = (:tipo)")
    fun getByTipo(tipo: Tipo): List<Ricetta>
}