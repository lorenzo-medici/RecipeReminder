package it.loremed.recipereminder.model.lista

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class ItemLista : Serializable {
    @PrimaryKey
    var id: Long = System.currentTimeMillis()

    @ColumnInfo(name = "oggetto")
    var oggetto: String

    @Ignore
    constructor() {
        oggetto = ""
    }

    constructor(oggetto: String) : this() {
        this.oggetto = oggetto
    }

    override fun equals(other: Any?): Boolean {
        return if (other is ItemLista) {
            id == other.id && oggetto == other.oggetto
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
