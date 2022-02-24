package it.loremed.recipereminder.view.ricette

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import it.loremed.recipereminder.EDIT_RESULT_CODE
import it.loremed.recipereminder.EXECUTE_RESULT_CODE
import it.loremed.recipereminder.R
import it.loremed.recipereminder.REMOVE_RESULT_CODE
import it.loremed.recipereminder.model.ricette.Ricetta
import it.loremed.recipereminder.model.ricette.Tipo

class ExistingRicettaActivity : AppCompatActivity() {

    private lateinit var editRicettaName: EditText
    private lateinit var editRicettaDescrizione: EditText
    private lateinit var editRicettaTipiContainer: RadioGroup

    private lateinit var ricettaMostrata: Ricetta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_existing_ricetta)

        editRicettaName = findViewById(R.id.nome_ricetta)
        editRicettaDescrizione = findViewById(R.id.descrizione_ricetta)
        editRicettaTipiContainer = findViewById(R.id.tipo_choice_container)

        val bundle = intent.extras

        if (bundle != null) {
            ricettaMostrata = bundle.get("ricetta") as Ricetta
        }

        val options = Tipo.values().map(Tipo::printable)

        for (i in options.indices) {
            val rb = RadioButton(this)
            rb.text = options[i]
            rb.id = View.generateViewId()
            rb.layoutParams = ViewGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
            )
            if (rb.text == ricettaMostrata.tipo.printable)
                rb.isChecked = true

            editRicettaTipiContainer.addView(rb)
        }


        editRicettaName.setText(ricettaMostrata.nome)
        editRicettaDescrizione.setText(ricettaMostrata.descrizione)

        val saveButton = findViewById<Button>(R.id.button_save)
        saveButton.setOnClickListener {

            if (editRicettaName.text.isEmpty()) {
                Toast.makeText(this, "Inserisci il nome della ricetta!", Toast.LENGTH_SHORT).show()
            } else {
                val replyIntent = Intent()
                val id = editRicettaTipiContainer.checkedRadioButtonId

                ricettaMostrata.nome = editRicettaName.text.toString()
                ricettaMostrata.descrizione = editRicettaDescrizione.text.toString()
                ricettaMostrata.tipo = Tipo.fromPrintable(findViewById<RadioButton>(id).text as String)

                replyIntent.putExtra(EXTRA_REPLY, ricettaMostrata)
                setResult(Activity.RESULT_FIRST_USER + EDIT_RESULT_CODE, replyIntent)
                finish()
            }
        }

        val executeButton = findViewById<Button>(R.id.button_esegui)
        executeButton.setOnClickListener {
            val replyIntent = Intent()

            replyIntent.putExtra(EXTRA_REPLY, ricettaMostrata)
            setResult(Activity.RESULT_FIRST_USER + EXECUTE_RESULT_CODE, replyIntent)
            finish()
        }

        val removeButton = findViewById<Button>(R.id.button_elimina)
        removeButton.setOnClickListener {
            val replyIntent = Intent()

            replyIntent.putExtra(EXTRA_REPLY, ricettaMostrata)
            setResult(Activity.RESULT_FIRST_USER + REMOVE_RESULT_CODE, replyIntent)
            finish()
        }

        val view = findViewById<LinearLayout>(R.id.existing_ricetta_linearlayout)
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