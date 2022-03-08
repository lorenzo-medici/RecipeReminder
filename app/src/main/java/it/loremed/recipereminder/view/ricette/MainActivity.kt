package it.loremed.recipereminder.view.ricette

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.loremed.recipereminder.*
import it.loremed.recipereminder.model.ricette.Ricetta
import it.loremed.recipereminder.model.ricette.Tipo
import it.loremed.recipereminder.view.SimpleGestureFilter
import it.loremed.recipereminder.view.lista.ListaActivity
import it.loremed.recipereminder.viewmodel.ricette.RicettaViewModel


class MainActivity : AppCompatActivity(), SimpleGestureFilter.SimpleGestureListener {

    private val ricettaViewModel: RicettaViewModel by viewModels {
        RicettaViewModel.RicettaViewModelFactory((application as RicettaApplication).ricettaRepository)
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

    private lateinit var detector: SimpleGestureFilter
    private var textViewType: TextView? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.custom_action_bar)
        val view: View = supportActionBar!!.customView

        textViewType = view.findViewById(R.id.recipe_type_text)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = RicettaListAdapter { position -> onListItemClick(position) }
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

                val titleString: String =
                    if (item.title.toString() == getString(R.string.all_recipe_types)) {
                        ""
                    } else {
                        item.title.toString()
                    }

                textViewType!!.text = titleString
                ricettaViewModel.setFilter(titleString)

                true
            }
            val options = Tipo.values().map(Tipo::plural)

            for (i in options.indices) {
                popupMenu.menu.add(options[i])
            }

            popupMenu.show()
        }

        detector = SimpleGestureFilter(this, this)

        val intent = Intent(this, ListaActivity::class.java)
        intent.putExtra("isShortcut", "true")
        intent.action = Intent.ACTION_VIEW

        val bitmap = convertAppIconDrawableToBitmap(
            this,
            AppCompatResources.getDrawable(this, R.mipmap.ic_shortcut_icon)!!
        )


        val shortcut = ShortcutInfoCompat.Builder(this, "id1")
            .setShortLabel("Aggiungi")
            .setLongLabel("Aggiungi un oggetto")
            .setDisabledMessage("Disattivato")
            .setIcon(IconCompat.createWithAdaptiveBitmap(bitmap))
            .setIntent(intent)
            .build()

        ShortcutManagerCompat.pushDynamicShortcut(this, shortcut)

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

    override fun onSwipe(direction: Int) {
        when (direction) {
            SimpleGestureFilter.SWIPE_LEFT -> {
                startActivity(Intent(this@MainActivity, ListaActivity::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                this@MainActivity.finish()
            }
            else -> {
                // Do nothing
            }
        }
    }

    override fun onDoubleTap() {
        Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show()
    }

    override fun dispatchTouchEvent(me: MotionEvent?): Boolean {
        this.detector.onTouchEvent(me!!)
        return super.dispatchTouchEvent(me)
    }

    private fun convertAppIconDrawableToBitmap(context: Context, drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable)
            return drawable.bitmap
        val appIconSize = if (drawable is AdaptiveIconDrawable)
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                108f,
                context.resources.displayMetrics
            ).toInt()
        else getAppIconSize(context)
        return drawable.toBitmap(appIconSize, appIconSize, Bitmap.Config.ARGB_8888)
    }

    private fun getAppIconSize(context: Context): Int {
        val activityManager = ContextCompat.getSystemService(this, ActivityManager::class.java)!!
        val appIconSize = try {
            activityManager.launcherLargeIconSize
        } catch (e: Exception) {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                48f,
                context.resources.displayMetrics
            ).toInt()
        }
        return appIconSize
    }


}
