package it.loremed.recipereminder.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import it.loremed.recipereminder.view.lista.ListaFragment
import it.loremed.recipereminder.view.ricette.RicettarioFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                RicettarioFragment()
            }
            1 -> {
                ListaFragment()
            }
            else -> {
                Fragment()
            }

        }
    }

}