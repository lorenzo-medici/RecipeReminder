package it.loremed.recipereminder.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import it.loremed.recipereminder.R
import it.loremed.recipereminder.view.adapters.ViewPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var textViewType: TextView
    private lateinit var viewPager2: ViewPager2
    private lateinit var onPageChangeCallbackHandler: OnPageChangeCallbackHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.custom_action_bar)
        val view: View = supportActionBar!!.customView

        textViewType = view.findViewById(R.id.recipe_type_text)

        viewPager2 = findViewById(R.id.view_pager_2)

        val clickFAB = intent.hasExtra("isShortcut")

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, clickFAB)

        viewPager2.adapter = adapter

        onPageChangeCallbackHandler = OnPageChangeCallbackHandler(textViewType)

        viewPager2.registerOnPageChangeCallback(onPageChangeCallbackHandler)

        if (clickFAB) {
            Log.d("SHORTCUT", "shortcut used")

            viewPager2.setCurrentItem(viewPager2.currentItem + 1, true)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager2.unregisterOnPageChangeCallback(onPageChangeCallbackHandler)
    }

    inner class OnPageChangeCallbackHandler(private val textViewHandler: TextView) :
        ViewPager2.OnPageChangeCallback() {

        private val fadeDuration: Long = 200

        override fun onPageSelected(position: Int) {

            when (position) {
                0 -> textViewHandler.animate().alpha(1.0f).duration = fadeDuration
                1 -> {
                    textViewHandler.animate().alpha(0.0f).duration = fadeDuration
                    // clickFABOnShortcut()
                }
            }
            Log.d("VIEWPAGER", "$position")
            super.onPageSelected(position)
        }
    }

}