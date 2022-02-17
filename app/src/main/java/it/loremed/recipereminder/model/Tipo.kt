package it.loremed.recipereminder.model

enum class Tipo {

    PRIMO, SECONDO, CONTORNO, PIATTO_UNICO, DOLCE, SNACK;

    override fun toString(): String {
        val toPrint = this.name
        return toPrint.substring(0, 1) + toPrint.substring(1).lowercase().replace('_', ' ')
    }
}