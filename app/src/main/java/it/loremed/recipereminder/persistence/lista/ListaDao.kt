package it.loremed.recipereminder.persistence.lista

import androidx.room.*
import it.loremed.recipereminder.model.lista.ItemLista
import kotlinx.coroutines.flow.Flow

@Dao
interface ListaDao {
    @Query("SELECT * FROM itemlista ORDER BY oggetto ASC")
    fun getAll(): Flow<List<ItemLista>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ricetta: ItemLista)

    @Delete
    suspend fun delete(ricetta: ItemLista)

    @Update
    suspend fun update(ricetta: ItemLista)
}