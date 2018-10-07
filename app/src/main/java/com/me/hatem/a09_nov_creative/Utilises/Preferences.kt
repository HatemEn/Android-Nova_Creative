package com.me.hatem.a09_nov_creative.Utilises

import android.app.Activity
import android.content.Context

class Preferences(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE) // MODE_PRIVATE = 0

    var wallpaper: String
        get() = prefs.getString(PREFS_NAME_WALLPAPER, DEFAULT)
        set(value) = prefs.edit().putString(PREFS_NAME_WALLPAPER, value).apply()

    var home_columns: Int
        get() = prefs.getInt(PREFS_NAME_HOME_COLUMNS, DEFAULT_NUM_COLUMNS_HOME)
        set(value) = prefs.edit().putInt(PREFS_NAME_HOME_COLUMNS, value).apply()

    var home_rows: Int
        get() = prefs.getInt(PREFS_NAME_HOME_ROWS, DEFAULT_NUM_ROWS_HOME)
        set(value) = prefs.edit().putInt(PREFS_NAME_HOME_ROWS, value).apply()
}