package it.loremed.recipereminder.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
class Ricetta : Serializable {

    @PrimaryKey var id: Long = System.currentTimeMillis()
    @ColumnInfo(name = "nome") var nome: String
    @ColumnInfo(name = "descrizione") var descrizione: String
    @ColumnInfo(name = "tipo") var tipo: Tipo
    @ColumnInfo(name = "ultimoUtilizzo") var ultimoUtilizzo: Date = Date()

    override fun equals(other: Any?): Boolean {
        return if (other is Ricetta) {
            id == other.id && nome == other.nome && descrizione == other.descrizione && tipo == other.tipo && ultimoUtilizzo == other.ultimoUtilizzo
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    private fun aggiornaUltimoUtilizzo() {
        ultimoUtilizzo = Date()
    }

    @Ignore
    constructor() {
        this.nome = ""
        this.descrizione = ""
        this.tipo = Tipo.DOLCE
    }

    constructor(nome: String, descrizione: String, tipo: Tipo) : this() {
        this.nome = nome
        this.descrizione = descrizione
        this.tipo = tipo
    }
}