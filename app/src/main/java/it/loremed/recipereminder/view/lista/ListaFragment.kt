package it.loremed.recipereminder.view.lista

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.loremed.recipereminder.R
import it.loremed.recipereminder.RicettaApplication
import it.loremed.recipereminder.model.lista.ItemLista
import it.loremed.recipereminder.viewmodel.lista.ListaViewModel

class ListaFragment : Fragment() {


    private val listaViewModel: ListaViewModel by activityViewModels {
        ListaViewModel.ListaViewModelFactory((requireActivity().application as RicettaApplication).listaRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_lista, container, false)

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerview_lista)
        val adapter = ListaListAdapter({ position -> onListItemBuyClick(position) },
            { position -> onListItemClick(position) })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        listaViewModel.allItems.observe(viewLifecycleOwner) { items ->
            items?.let { adapter.submitList(it) }
        }

        val fab = rootView.findViewById<FloatingActionButton>(R.id.add_item_button)
        fab.setOnClickListener {

            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            val customAlertDialog = View.inflate(
                requireContext(),
                R.layout.custom_alert_dialog, null
            )
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


        return rootView
    }


    private fun onListItemBuyClick(position: Int) {
        val itemDaEliminare = listaViewModel.allItems.value?.get(position)

        listaViewModel.delete(itemDaEliminare!!)
    }

    private fun onListItemClick(position: Int) {
        val itemDaModificare = listaViewModel.allItems.value?.get(position)

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val customAlertDialog = View.inflate(requireContext(), R.layout.custom_alert_dialog, null)
        val inputText = customAlertDialog.findViewById<EditText>(R.id.editText)

        val alertDialog = with(builder) {
            setTitle("Modifica un oggetto:")

            setView(customAlertDialog)

            setPositiveButton("Modifica") { _, _ ->
                itemDaModificare!!.oggetto = inputText.text.toString()
                listaViewModel.update(itemDaModificare)
            }

            setNegativeButton("Cancella") { dialog, _ ->
                dialog.cancel()
            }
        }.create()

        alertDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        alertDialog.setOnShowListener {
            customAlertDialog.requestFocus()
            inputText.setSelection(inputText.length())
        }

        inputText.setText(itemDaModificare!!.oggetto)

        inputText.doOnTextChanged { _: CharSequence?, _: Int, _: Int, _: Int ->
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                inputText.text.isNotEmpty()
        }

        alertDialog.show()
    }
}