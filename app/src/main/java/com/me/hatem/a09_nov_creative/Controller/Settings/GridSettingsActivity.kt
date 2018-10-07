package com.me.hatem.a09_nov_creative.Controller.Settings

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import com.me.hatem.a09_nov_creative.R
import com.me.hatem.a09_nov_creative.Utilises.COLS_ROWS_SEEK_BAR_SHIFT
import com.me.hatem.a09_nov_creative.Utilises.Preferences
import kotlinx.android.synthetic.main.activity_grid_settings.*


class GridSettingsActivity : AppCompatActivity() {
    lateinit var preferences: Preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_settings)


        preferences = Preferences(this)
        columnsNumSeekBar.progress  = preferences.home_columns // if not set already it will take the default
        rowsNumSeekBar.progress     = preferences.home_rows // if not set already it will take the default

        Toast.makeText(this, columnsNumSeekBar.progress.toString(), Toast.LENGTH_SHORT).show()
        columnsNumSeekBar.setOnSeekBarChangeListener(SeekBarListener(this))
        rowsNumSeekBar.setOnSeekBarChangeListener(SeekBarListener(this))

    }



    class SeekBarListener(val context: Context) : SeekBar.OnSeekBarChangeListener {
        val preferences = Preferences(context)
        override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (seekbar?.tag == "columns") preferences.home_columns = progress
            if (seekbar?.tag == "rows") preferences.home_rows = progress
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
        }

    }
}


