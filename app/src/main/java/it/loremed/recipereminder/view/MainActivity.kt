package it.loremed.recipereminder.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.loremed.recipereminder.R
import it.loremed.recipereminder.RicettaApplication
import it.loremed.recipereminder.viewmodel.RicettaViewModel
import androidx.lifecycle.Observer
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.loremed.recipereminder.model.Ricetta

class MainActivity : AppCompatActivity() {

    private val ricettaViewModel: RicettaViewModel by viewModels {
        RicettaViewModel.RicettaViewModelFactory((application as RicettaApplication).repository)
    }

    private val newRicettaActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = RicettaListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // crazione dell'Observer
        ricettaViewModel.allRecipes.observe(this, Observer { ricette ->
            // Update the cached copy of the words in the adapter
            ricette?.let { adapter.submitList(it)}
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewRicettaActivity::class.java)
            startActivityForResult(intent, newRicettaActivityRequestCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newRicettaActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getSerializableExtra(NewRicettaActivity.EXTRA_REPLY)?.let {
                val ricetta = it as Ricetta
                ricettaViewModel.insert(ricetta)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }



}