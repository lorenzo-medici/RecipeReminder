package it.loremed.recipereminder.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.loremed.recipereminder.*
import it.loremed.recipereminder.model.Ricetta
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = RicettaListAdapter { position -> onListItemClick(position) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // crazione dell'Observer
        ricettaViewModel.allRecipes.observe(this) { ricette ->
            // Update the cached copy of the words in the adapter
            ricette?.let { adapter.submitList(it) }
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewRicettaActivity::class.java)
            newRicettaResultLauncher.launch(intent)
        }

    }


    private fun onListItemClick(position: Int) {

        Log.d("EDIT_RICETTA", "Opening new ricetta as edit ricetta position $position")

        // Ottenere la ricetta clickata a partire dalla posizione
        val ricettaSelezionata = ricettaViewModel.allRecipes.value?.get(position)

        Log.d("EDIT_RICETTA", "ricetta da modificare $ricettaSelezionata")

        // Aprire la Activity e inizializzare i campi
        val intent = Intent(this@MainActivity, ExistingRicettaActivity::class.java)
        intent.putExtra("ricetta", ricettaSelezionata)
        existingRicettaResultLauncher.launch(intent)
    }
}