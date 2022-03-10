package it.loremed.recipereminder.view

import android.util.Log
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2

class OnPageChangeCallbackHandler(private val textViewHandler: TextView) :
    ViewPager2.OnPageChangeCallback() {

    private val fadeDuration: Long = 200

    override fun onPageSelected(position: Int) {

        when (position) {
            0 -> textViewHandler.animate().alpha(1.0f).duration = fadeDuration
            1 -> textViewHandler.animate().alpha(0.0f).duration = fadeDuration
        }
        Log.d("VIEWPAGER", "$position")
        super.onPageSelected(position)
    }
}