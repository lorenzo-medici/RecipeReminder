package it.loremed.recipereminder.model.ricette

enum class Tipo(val printable: String, val plural: String) {

    PRIMO("Primo", "Primi"), SECONDO("Secondo", "Secondi"), CONTORNO(
        "Contorno",
        "Contorni"
    ),
    PIATTO_UNICO("Piatto unico", "Piatti unici"), DOLCE("Dolce", "Dolci"), SNACK("Snack", "Snack");

    companion object {

        fun fromPrintable(printable: String): Tipo {
            return values().filter { it.printable == printable }[0]
        }

        fun fromPlural(plural: String): Tipo {
            return values().filter { it.plural == plural }[0]
        }
    }
}