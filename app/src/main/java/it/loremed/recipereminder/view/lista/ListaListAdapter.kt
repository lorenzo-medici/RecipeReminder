package it.loremed.recipereminder.view.lista

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import it.loremed.recipereminder.R
import it.loremed.recipereminder.model.lista.ItemLista

class ListaListAdapter(private val onItemClicked: (position: Int) -> Unit) :
    ListAdapter<ItemLista, ListaListAdapter.ListaViewHolder>(ItemsComparator()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaViewHolder {
        return ListaViewHolder.create(parent, onItemClicked)
    }

    override fun onBindViewHolder(holder: ListaViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.oggetto)
    }


    class ListaViewHolder(itemView: View, private val onItemClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val listaItemView: TextView = itemView.findViewById(R.id.textview_lista)
        private val listaItemDeleteButton: AppCompatImageButton =
            itemView.findViewById(R.id.imagebutton_lista)

        init {
            listaItemDeleteButton.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
            }
        }

        fun bind(text: String?) {
            listaItemView.text = text
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClicked: (position: Int) -> Unit
            ): ListaViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item_lista, parent, false)
                return ListaViewHolder(view, onItemClicked)
            }
        }
    }

    class ItemsComparator : DiffUtil.ItemCallback<ItemLista>() {

        override fun areItemsTheSame(oldItem: ItemLista, newItem: ItemLista): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ItemLista, newItem: ItemLista): Boolean {
            return oldItem.oggetto == newItem.oggetto
        }
    }
}
