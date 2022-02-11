package it.loremed.recipereminder.model


class Ricettario {

    var ricette: MutableList<Ricetta> = mutableListOf()

    fun aggiungiRicetta(ricetta: Ricetta): Boolean {
        return ricette.add(ricetta)
    }

    fun eliminaRicetta(ricetta: Ricetta): Boolean {
        return ricette.remove(ricetta)
    }

    companion object {
        @JvmStatic
        var instance: Ricettario? = null
            get() {
                if (field == null) {
                    field = Ricettario()
                }
                return field
            }
            private set
    }
}