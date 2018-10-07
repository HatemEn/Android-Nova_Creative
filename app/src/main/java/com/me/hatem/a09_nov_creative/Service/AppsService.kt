package com.me.hatem.a09_nov_creative.Service

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.Resources
import android.view.View
import android.widget.Toast
import com.me.hatem.a09_nov_creative.Model.App
import com.me.hatem.a09_nov_creative.Model.HomePage
import com.me.hatem.a09_nov_creative.R
import kotlinx.android.synthetic.main.item_app.view.*

object AppsService {
    // access variables
    val installedApps   = ArrayList<App>()
    val homeApps        = ArrayList<HomePage>()

    // methods
    fun getInstalledApps(context: Context) {
        val installedApps = ArrayList<App>()
        // get the apps from android phone
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val apps = context.packageManager.queryIntentActivities(intent, 0) as List<ResolveInfo>
        for (app in apps) {
            val appName = app.activityInfo.loadLabel(context.packageManager).toString()
            val appIcon = app.activityInfo.loadIcon(context.packageManager)
            val packageName = app.activityInfo.packageName
            val installedApp = App(packageName, appName, appIcon)
            if (installedApp !in installedApps) installedApps.add(installedApp)
        }

        this.installedApps.addAll(installedApps)
    }


    fun getHomePages(resources: Resources, numColumns_home: Int, numRow_home: Int) {
        val homePages = ArrayList<HomePage>()
        val apps1 = ArrayList<App>()
        val apps2 = ArrayList<App>()
        val apps3 = ArrayList<App>()

        for (i in 0 until numColumns_home * numRow_home) {
            // apps name have the index of the cell
            apps1.add(App("$i", "", resources.getDrawable(R.drawable.empty_icon)))
            apps2.add(App("$i", "", resources.getDrawable(R.drawable.empty_icon)))
            apps3.add(App("$i", "", resources.getDrawable(android.R.drawable.star_on)))
        }
        homePages.add(HomePage(apps1))
        homePages.add(HomePage(apps2))
        homePages.add(HomePage(apps3))

        this.homeApps.addAll(homePages)
    }

    fun putAppOnHome(context: Context, view: View, packageName: String) {
        if (view.appLabel.text.isNullOrEmpty()) {
            val packageManager = context.packageManager
            try {
                val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)

                val appName = appInfo?.loadLabel(packageManager).toString()
                val appIcon = appInfo?.loadIcon(packageManager)
                val app = App(packageName, appName, appIcon!!)
                view.appLabel.text = app.name
                view.appIcon.setImageDrawable(app.icon)
                view.appPackageName.tag = packageName
            } catch (e: Exception) {
                println(e)
            }
        } else {
            Toast.makeText(context, "The cell is taken of!", Toast.LENGTH_SHORT).show()
        }
    }

}