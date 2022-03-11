package it.loremed.recipereminder

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import it.loremed.recipereminder.persistence.lista.ListaRepository
import it.loremed.recipereminder.persistence.lista.ListaRoomDatabase
import it.loremed.recipereminder.persistence.ricette.RicettaRepository
import it.loremed.recipereminder.persistence.ricette.RicettaRoomDatabase
import it.loremed.recipereminder.view.MainActivity

class RicettaApplication : Application() {

    private val ricettaDatabase by lazy { RicettaRoomDatabase.getDatabase(this) }
    val ricettaRepository by lazy { RicettaRepository(ricettaDatabase.ricettaDao()) }

    private val listaDatabase by lazy { ListaRoomDatabase.getDatabase(this) }
    val listaRepository by lazy { ListaRepository(listaDatabase.listaDao()) }

    override fun onCreate() {
        super.onCreate()

        if (ShortcutManagerCompat.getShortcuts(
                this,
                ShortcutManagerCompat.FLAG_MATCH_DYNAMIC
            ).size == 0
        ) {

            Log.d("SHORTCUT", "adding shortcut")
            val intent = Intent(this, MainActivity::class.java)
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