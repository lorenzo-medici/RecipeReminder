package it.loremed.recipereminder.persistence

import androidx.room.*
import it.loremed.recipereminder.model.Ricetta
import it.loremed.recipereminder.model.Tipo
import kotlinx.coroutines.flow.Flow

@Dao
interface RicettaDao {
    @Query("SELECT * FROM ricetta")
    fun getAll(): Flow<List<Ricetta>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ricetta: Ricetta)

    @Delete
    fun delete(ricetta: Ricetta)

    @Query("SELECT * FROM ricetta WHERE tipo = (:tipo)")
    fun getByTipo(tipo: Tipo): List<Ricetta>
}