package com.me.hatem.a09_nov_creative.Controller.Settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.me.hatem.a09_nov_creative.R
import com.me.hatem.a09_nov_creative.Utilises.PICK_WALLPAPER_REQUEST_CODE
import com.me.hatem.a09_nov_creative.Utilises.Preferences
import kotlinx.android.synthetic.main.activity_wallpaper_settings.*

class WallpaperSettingsActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallpaper_settings)

        preferences = Preferences(this)
        if (!preferences.wallpaper.isNullOrBlank()) launcherWallpaper.setImageURI(Uri.parse(preferences.wallpaper))

        getPermissions()
    }

    private fun getPermissions() {
        val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) requestPermissions(permissions,1)
    }

    fun browseGalleryClicked(view: View) {
        val pickWallpaperIntent = Intent(Intent.ACTION_PICK)
        pickWallpaperIntent.type = "image/*"
        startActivityForResult(pickWallpaperIntent, PICK_WALLPAPER_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_WALLPAPER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            launcherWallpaper.setImageURI(data?.data)
            launcherWallpaper.tag = data?.data.toString()
        }
    }

    fun setWallpaperClicked(view: View) {
        preferences.wallpaper = launcherWallpaper.tag.toString()
    }
}
