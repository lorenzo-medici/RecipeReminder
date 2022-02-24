package it.loremed.recipereminder.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.loremed.recipereminder.*
import it.loremed.recipereminder.model.Ricetta
import it.loremed.recipereminder.model.Tipo
import it.loremed.recipereminder.viewmodel.RicettaViewModel


class MainActivity : AppCompatActivity() {

    private val ricettaViewModel: RicettaViewModel by viewModels {
        RicettaViewModel.RicettaViewModelFactory((application as RicettaApplication).repository)
    }

    private var newRicettaResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getSerializableExtra(NewRicettaActivity.EXTRA_REPLY)?.let {
                    val ricetta = it as Ricetta
                    ricettaViewModel.insert(ricetta)
                }
            }
        }

    private var existingRicettaResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_FIRST_USER + EDIT_RESULT_CODE) {
                result.data?.getSerializableExtra(ExistingRicettaActivity.EXTRA_REPLY)?.let {
                    val ricetta = it as Ricetta
                    ricettaViewModel.update(ricetta)
                }
            }

            if (result.resultCode == Activity.RESULT_FIRST_USER + EXECUTE_RESULT_CODE) {
                result.data?.getSerializableExtra(ExistingRicettaActivity.EXTRA_REPLY)?.let {
                    val ricetta = it as Ricetta
                    ricetta.eseguiRicetta()
                    ricettaViewModel.update(ricetta)
                }
            }

            if (result.resultCode == Activity.RESULT_FIRST_USER + REMOVE_RESULT_CODE) {
                result.data?.getSerializableExtra(ExistingRicettaActivity.EXTRA_REPLY)?.let {
                    val ricetta = it as Ricetta
                    ricettaViewModel.delete(ricetta)
                }
            }

        }

    private var textViewType: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.custom_action_bar)
        val view: View = supportActionBar!!.customView

        textViewType = view.findViewById(R.id.recipe_type_text)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = RicettaListAdapter { position -> onListItemClick(position)}
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // crazione dell'Observer
        ricettaViewModel.allRecipesFiltered.observe(this) { ricette ->
            // Update the cached copy of the words in the adapter
            ricette?.let { adapter.submitList(it) }
        }

        val fab = findViewById<FloatingActionButton>(R.id.add_recipe_button)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewRicettaActivity::class.java)
            newRicettaResultLauncher.launch(intent)
        }

        val filterFab = findViewById<FloatingActionButton>(R.id.filter_type_button)
        filterFab.setOnClickListener {
            val popupMenu = PopupMenu(this, filterFab)
            popupMenu.menuInflater.inflate(R.menu.type_choose_menu, popupMenu.menu)
                       popupMenu.setOnMenuItemClickListener { item ->

                           Log.d("FILTERING", "Item title selected ${item.title}")

                           val filterString: String
                           val titleString: String

                           if (item.title.toString() == getString(R.string.all_recipe_types)) {
                               filterString = ""
                               titleString = ""
                           } else {
                               val tipo = Tipo.fromPlural(item.title.toString())
                               filterString = tipo.toString()
                               titleString = tipo.plural
                           }

                           textViewType!!.text = titleString
                           ricettaViewModel.setFilter(filterString)

                           true
                       }
            val options = Tipo.values().map(Tipo::plural)

            for (i in options.indices) {
                popupMenu.menu.add(options[i])
            }

            popupMenu.show()
        }
    }


    private fun onListItemClick(position: Int) {

        Log.d("EDIT_RICETTA", "Opening new ricetta as edit ricetta position $position")

        // Ottenere la ricetta clickata a partire dalla posizione
        val ricettaSelezionata = ricettaViewModel.allRecipesFiltered.value?.get(position)

        Log.d("EDIT_RICETTA", "ricetta da modificare $ricettaSelezionata")

        // Aprire la Activity e inizializzare i campi
        val intent = Intent(this@MainActivity, ExistingRicettaActivity::class.java)
        intent.putExtra("ricetta", ricettaSelezionata)
        existingRicettaResultLauncher.launch(intent)
    }
}

/* TODO     - Spostare bottone filtro nella action bar
            - Implementare lista della spesa
 */
