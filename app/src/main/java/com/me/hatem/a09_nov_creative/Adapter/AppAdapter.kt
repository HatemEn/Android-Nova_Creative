package com.me.hatem.a09_nov_creative.Adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.me.hatem.a09_nov_creative.Model.App
import com.me.hatem.a09_nov_creative.R
import com.me.hatem.a09_nov_creative.Service.DragAndDropService
import kotlinx.android.synthetic.main.item_app.view.*

class AppAdapter(val context: Context, var apps: ArrayList<App>, val cellHeight: Int) : BaseAdapter() {
    val tempApps = apps


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v : View
        if (convertView == null)
            v = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false)
        else v = convertView

        v.appIcon.setImageDrawable(apps[position].icon)
        v.appLabel.text = apps[position].name

        v.appPackageName.tag = apps[position].packageName
        // Drop listener
        v.appLayout.setOnDragListener { view, dragEvent ->
            DragAndDropService.dragListener(context, view, dragEvent)
            return@setOnDragListener true
        }


        val lp = AbsListView.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, cellHeight)
        v.appLayout.layoutParams = lp
        return v;
    }

    override fun getItem(position: Int): Any {
        return apps[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return apps.count()
    }


    fun filterApps(text: String) {
        apps = ArrayList() // main thing since "clear()" didn't work
        if (text.isNullOrEmpty()) {
            apps.addAll(tempApps)
        } else {
            val search = text.toLowerCase()
            for (app in tempApps) {
                if (app.name.toLowerCase().contains(search)) {
                    apps.add(app)
                }
            }
        }
        notifyDataSetChanged()
    }
}