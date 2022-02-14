package it.loremed.recipereminder.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import it.loremed.recipereminder.R
import it.loremed.recipereminder.model.Ricetta
import it.loremed.recipereminder.model.Tipo

class NewRicettaActivity : AppCompatActivity() {

    private lateinit var editRicettaName: EditText
    private lateinit var editRicettaDescrizione: EditText
    private lateinit var editRicettaTipiContainer: RadioGroup
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_ricetta)

        editRicettaName = findViewById(R.id.nome_ricetta)
        editRicettaDescrizione = findViewById(R.id.descrizione_ricetta)
        editRicettaTipiContainer = findViewById(R.id.tipo_choice_container)

        val options = Tipo.values().map(Tipo::toString)

        for (i in options.indices) {
            val rb = RadioButton(this)
            rb.text = options[i]
            rb.id = View.generateViewId()
            editRicettaTipiContainer.addView(rb)
        }

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()

            val id = editRicettaTipiContainer.checkedRadioButtonId

            if (id == -1) {
                // TODO Display toast, senza uscire
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val nome = editRicettaName.text.toString()
                val descrizione = editRicettaDescrizione.text.toString()
                val tipo = Tipo.valueOf((findViewById<RadioButton>(id).text as String).uppercase().replace(' ', '_'))
                replyIntent.putExtra(EXTRA_REPLY, Ricetta(nome, descrizione, tipo))
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }


    companion object {
        const val EXTRA_REPLY = "com.example.android.ricettalistsql.REPLY"
    }

}