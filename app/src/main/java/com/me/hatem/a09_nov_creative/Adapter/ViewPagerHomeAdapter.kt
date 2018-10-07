package com.me.hatem.a09_nov_creative.Adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.me.hatem.a09_nov_creative.Controller.MainActivity
import com.me.hatem.a09_nov_creative.Model.HomePage
import com.me.hatem.a09_nov_creative.R
import com.me.hatem.a09_nov_creative.Service.DragAndDropService
import kotlinx.android.synthetic.main.item_app.view.*
import kotlinx.android.synthetic.main.pager_layout.view.*

class ViewPagerHomeAdapter(val context: Context, val homePages: List<HomePage>, val cellHeight: Int, val numColumns: Int) : PagerAdapter() {

    val appAdapterList = ArrayList<AppAdapter>()
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = LayoutInflater.from(context).inflate(R.layout.pager_layout, container, false)

        val appAdapter = AppAdapter(context, homePages[position].apps, cellHeight)
        appAdapterList.add(appAdapter)
        v.homeGridLayout.numColumns = numColumns
        v.homeGridLayout.adapter = appAdapter

        v.homeGridLayout.setOnItemClickListener { adapterView, view, i, l ->
            //return@setOnItemClickListener
            if (context is MainActivity) {
               // (context as MainActivity).appHomeClicked(homePages[position].apps[i])
            }
        }

        v.homeGridLayout.setOnItemLongClickListener { adapterView, view, i, l ->
            DragAndDropService.startDrag(view)
            view.appLabel.text = ""
            view.appPackageName.tag = ""
            view.appIcon.setImageDrawable(context.resources.getDrawable(R.drawable.empty_icon))
           // if (context is MainActivity) (context as MainActivity).appHomeLongClicked(homePages[position].apps[i])

            return@setOnItemLongClickListener true
        }
        container.addView(v)
        return v
    }



    override fun getCount(): Int {
        return homePages.count()
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun notifyAppHomeChange() {
        for (i in 0 until appAdapterList.count())
            appAdapterList[i].notifyDataSetChanged()
    }
}