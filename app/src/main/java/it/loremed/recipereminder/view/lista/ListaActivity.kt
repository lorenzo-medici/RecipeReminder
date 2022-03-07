package it.loremed.recipereminder.view.lista

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.loremed.recipereminder.R
import it.loremed.recipereminder.RicettaApplication
import it.loremed.recipereminder.model.lista.ItemLista
import it.loremed.recipereminder.view.SimpleGestureFilter
import it.loremed.recipereminder.view.ricette.MainActivity
import it.loremed.recipereminder.viewmodel.lista.ListaViewModel


class ListaActivity : AppCompatActivity(), SimpleGestureFilter.SimpleGestureListener {

    private lateinit var detector: SimpleGestureFilter

    private val listaViewModel: ListaViewModel by viewModels {
        ListaViewModel.ListaViewModelFactory((application as RicettaApplication).listaRepository)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_della_spesa)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_lista)
        val adapter = ListaListAdapter { position -> onListItemClick(position) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        listaViewModel.allItems.observe(this) { items ->
            items?.let { adapter.submitList(it) }
        }

        val fab = findViewById<FloatingActionButton>(R.id.add_item_button)
        fab.setOnClickListener {

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            val customAlertDialog = View.inflate(this, R.layout.custom_alert_dialog, null)
            val inputText = customAlertDialog.findViewById<EditText>(R.id.editText)

            val alertDialog = with(builder) {
                setTitle("Aggiungi un oggetto:")

                setView(customAlertDialog)

                setPositiveButton("Aggiungi") { _, _ ->
                    listaViewModel.insert(ItemLista(inputText.text.toString()))
                }

                setNegativeButton("Cancella") { dialog, _ ->
                    dialog.cancel()
                }
            }.create()

            alertDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

            alertDialog.setOnShowListener {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                customAlertDialog.requestFocus()
            }

            inputText.doOnTextChanged { _: CharSequence?, _: Int, _: Int, _: Int ->
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                    inputText.text.isNotEmpty()
            }

            alertDialog.show()

        }

        detector = SimpleGestureFilter(this, this)
    }

    private fun onListItemClick(position: Any) {
        val itemDaEliminare = listaViewModel.allItems.value?.get(position as Int)

        listaViewModel.delete(itemDaEliminare!!)
    }

    override fun onSwipe(direction: Int) {
        when (direction) {
            SimpleGestureFilter.SWIPE_RIGHT -> {
                startActivity(Intent(this@ListaActivity, MainActivity::class.java))
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                this@ListaActivity.finish()
            }
            else -> {
                // Do nothing
            }
        }
    }


    override fun onDoubleTap() {
        Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show()
    }

    override fun dispatchTouchEvent(me: MotionEvent?): Boolean {
        this.detector.onTouchEvent(me!!)
        return super.dispatchTouchEvent(me)
    }

}