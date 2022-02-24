package it.loremed.recipereminder.persistence

import androidx.room.*
import it.loremed.recipereminder.model.Ricetta
import kotlinx.coroutines.flow.Flow

@Dao
interface RicettaDao {
    @Query("SELECT * FROM ricetta ORDER BY ultimoUtilizzo ASC")
    fun getAll(): Flow<List<Ricetta>>

    @Query("SELECT * FROM ricetta WHERE tipo LIKE :tipoQuery ORDER BY ultimoUtilizzo ASC")
    fun getByTipo(tipoQuery: String): Flow<List<Ricetta>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ricetta: Ricetta)

    @Delete
    suspend fun delete(ricetta: Ricetta)

    @Update
    suspend fun update(ricetta: Ricetta)
}