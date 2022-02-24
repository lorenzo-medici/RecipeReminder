package it.loremed.recipereminder.view.ricette

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import it.loremed.recipereminder.R
import it.loremed.recipereminder.model.ricette.Ricetta
import it.loremed.recipereminder.model.ricette.Tipo


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

        val options = Tipo.values().map(Tipo::printable)

        for (i in options.indices) {
            val rb = RadioButton(this)
            rb.text = options[i]
            rb.id = View.generateViewId()
            rb.layoutParams = ViewGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
            )
            editRicettaTipiContainer.addView(rb)
        }

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {

            val id = editRicettaTipiContainer.checkedRadioButtonId

            if (id == -1 || editRicettaName.text.isEmpty()) {
                Toast.makeText(this, "Seleziona un tipo di ricetta!", Toast.LENGTH_SHORT).show()
            } else {
                val replyIntent = Intent()
                val nome = editRicettaName.text.toString()
                val descrizione = editRicettaDescrizione.text.toString()
                val tipo = Tipo.fromPrintable(findViewById<RadioButton>(id).text as String)

                replyIntent.putExtra(EXTRA_REPLY, Ricetta(nome, descrizione, tipo))
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
        }

        val view = findViewById<LinearLayout>(R.id.new_ricetta_linearlayout)

        hideKeyboardOnOutSideTouch(view)

    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun hideKeyboardOnOutSideTouch(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { v, _ ->
                v.performClick()
                hideKeyboard(view)
                false
            }
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                hideKeyboardOnOutSideTouch(innerView)
            }
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.ricettalistsql.REPLY"
    }

}