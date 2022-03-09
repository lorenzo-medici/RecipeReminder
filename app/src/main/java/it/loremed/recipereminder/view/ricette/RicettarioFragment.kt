package it.loremed.recipereminder.view.ricette

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.loremed.recipereminder.*
import it.loremed.recipereminder.model.ricette.Ricetta
import it.loremed.recipereminder.model.ricette.Tipo
import it.loremed.recipereminder.viewmodel.ricette.RicettaViewModel

class RicettarioFragment : Fragment() {

    private val ricettaViewModel: RicettaViewModel by activityViewModels {
        RicettaViewModel.RicettaViewModelFactory((requireActivity().application as RicettaApplication).ricettaRepository)
    }

    private lateinit var textViewType: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Potrebbe funzionare se la Activity principale contiene il campo nella actionbar
        textViewType = requireActivity().findViewById(R.id.recipe_type_text)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_ricettario, container, false)

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = RicettaListAdapter { position -> onListItemClick(position) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // crazione dell'Observer
        ricettaViewModel.allRecipesFiltered.observe(viewLifecycleOwner) { ricette ->
            // Update the cached copy of the words in the adapter
            ricette?.let { adapter.submitList(it) }
        }

        val fab = rootView.findViewById<FloatingActionButton>(R.id.add_recipe_button)
        fab.setOnClickListener {
            val intent = Intent(requireContext(), NewRicettaActivity::class.java)
            newRicettaResultLauncher.launch(intent)
        }

        val filterFab = rootView.findViewById<FloatingActionButton>(R.id.filter_type_button)
        filterFab.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), filterFab)
            popupMenu.menuInflater.inflate(R.menu.type_choose_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->

                Log.d("FILTERING", "Item title selected ${item.title}")

                val titleString: String =
                    if (item.title.toString() == getString(R.string.all_recipe_types)) {
                        ""
                    } else {
                        item.title.toString()
                    }

                textViewType.text = titleString
                ricettaViewModel.setFilter(titleString)

                true
            }
            val options = Tipo.values().map(Tipo::plural)

            for (i in options.indices) {
                popupMenu.menu.add(options[i])
            }

            popupMenu.show()
        }



        return rootView

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

    private fun onListItemClick(position: Int) {

        Log.d("EDIT_RICETTA", "Opening new ricetta as edit ricetta position $position")

        // Ottenere la ricetta clickata a partire dalla posizione
        val ricettaSelezionata = ricettaViewModel.allRecipesFiltered.value?.get(position)

        Log.d("EDIT_RICETTA", "ricetta da modificare $ricettaSelezionata")

        // Aprire la Activity e inizializzare i campi
        val intent = Intent(requireContext(), ExistingRicettaActivity::class.java)
        intent.putExtra("ricetta", ricettaSelezionata)
        existingRicettaResultLauncher.launch(intent)
    }

}