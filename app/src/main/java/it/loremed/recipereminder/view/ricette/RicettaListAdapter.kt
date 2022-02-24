package it.loremed.recipereminder.view.ricette

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import it.loremed.recipereminder.R
import it.loremed.recipereminder.model.ricette.Ricetta

class RicettaListAdapter(private val onItemClicked: (position: Int) -> Unit) :
    ListAdapter<Ricetta, RicettaListAdapter.RicettaViewHolder>(RicettasComparator()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RicettaViewHolder {
        return RicettaViewHolder.create(parent, onItemClicked)
    }

    override fun onBindViewHolder(holder: RicettaViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.nome)
    }

    class RicettaViewHolder(itemView: View, private val onItemClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val ricettaItemView: TextView = itemView.findViewById(R.id.textView)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(text: String?) {
            ricettaItemView.text = text
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClicked: (position: Int) -> Unit
            ): RicettaViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return RicettaViewHolder(view, onItemClicked)
            }
        }

        override fun onClick(p0: View?) {
            val position = bindingAdapterPosition
            onItemClicked(position)
        }
    }

    class RicettasComparator : DiffUtil.ItemCallback<Ricetta>() {
        override fun areItemsTheSame(oldItem: Ricetta, newItem: Ricetta): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Ricetta, newItem: Ricetta): Boolean {
            return oldItem.nome == newItem.nome
        }
    }
}
