package com.me.hatem.a09_nov_creative.Controller.Settings

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.me.hatem.a09_nov_creative.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setBackgroundBtn.setOnClickListener { startActivity(Intent(this, WallpaperSettingsActivity::class.java)) }
        setGridBtn.setOnClickListener { startActivity(Intent(this, GridSettingsActivity::class.java)) }
    }
}
