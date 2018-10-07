package com.me.hatem.a09_nov_creative.Controller

import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Point
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.SearchView
import android.widget.Toast
import com.me.hatem.a09_nov_creative.Adapter.AppAdapter
import com.me.hatem.a09_nov_creative.Adapter.ViewPagerHomeAdapter
import com.me.hatem.a09_nov_creative.Controller.Settings.SettingsActivity
import com.me.hatem.a09_nov_creative.Controller.Settings.WallpaperSettingsActivity
import com.me.hatem.a09_nov_creative.Model.App
import com.me.hatem.a09_nov_creative.R
import com.me.hatem.a09_nov_creative.Service.AppsService
import com.me.hatem.a09_nov_creative.Service.DragAndDropService
import com.me.hatem.a09_nov_creative.Utilises.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.item_app.view.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {


    var selectedAppForDrag: App? = null
    lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    lateinit var viewPagerHomeAdapter: ViewPagerHomeAdapter
    var screen: String = HOME_SCREEN // to distinguish between home click or drawer click
    private lateinit var preferences: Preferences
    // Home
    var numRow_home         = 0
    var numColumns_home     = 0
    var cellHeight_home     = 0
    // Drawer
    lateinit var appAdapter: AppAdapter
    var numRow_drawer       = DEFAULT_NUM_ROWS_DRAWER
    var numColumns_drawer   = DEFAULT_NUM_COLUMNS_DRAWER
    var cellHeight_drawer   = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setupPrefs()
        /* to get the height of cardView in drawer layout which get 100
        and add 32 for 16Dp margin in the grid layout at the bottom than it will be 132
        for the constant DRAWER_PEEK_HEIGHT and i use the following code to determine the height*/
        /*cardView.post {
            Toast.makeText(this, cardView.height.toString(), Toast.LENGTH_SHORT).show()
        }*/

        initializeHome()
        initializeDrawer();
    }

    private fun initializeHome() {
        AppsService.getHomePages(resources, numColumns_home, numRow_home) // load apps by service
        cellHeight_home = (getDisplayContentHeight() - DRAWER_PEEK_HEIGHT) / numRow_home
        viewPagerHomeAdapter = ViewPagerHomeAdapter(this, AppsService.homeApps, cellHeight_home, numColumns_home)
        viewPager.adapter = viewPagerHomeAdapter
    }


    private fun initializeDrawer() {
        cellHeight_drawer = (getDisplayContentHeight()) / numRow_drawer
        gridLayout.numColumns = numColumns_drawer
        // get installed apps

        AppsService.getInstalledApps(this)
        appAdapter = AppAdapter(this, AppsService.installedApps, cellHeight_drawer)
        gridLayout.adapter = appAdapter;

        gridLayout.setOnItemClickListener { adapterView, view, i, l ->
            val openApp = this.packageManager.getLaunchIntentForPackage(view.appPackageName.tag.toString())
            startActivity(openApp)
        }

        // make shortcut for the app in home screen when long press on the app in the drawer
        gridLayout.setOnItemLongClickListener { adapterView, view, i, l ->
            appDrawerClicked(AppsService.installedApps[i], view)
            return@setOnItemLongClickListener true
        }

        // access to bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.peekHeight = DRAWER_PEEK_HEIGHT.toInt()

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                /* to solve the problems of drawer when open or when try to close it */
                if (newState == BottomSheetBehavior.STATE_COLLAPSED && gridLayout.getChildAt(0).y != 0f)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

                if (newState == BottomSheetBehavior.STATE_DRAGGING && gridLayout.getChildAt(0).y != 0f)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

        })

        drawerAppsSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                appAdapter.filterApps(p0!!)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                appAdapter.filterApps(p0!!)
                return true
            }

        })
    }

    private fun appDrawerClicked(app: App, view: View){
        //first collapse the drawer
        gridLayout.y = DRAWER_PEEK_HEIGHT.toFloat() // to prevent overlapping between cardVIew and gridLayout
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        DragAndDropService.startDrag(view)
        //selectedApp(app, DRAWER_SCREEN)

    }


    fun appHomeClicked(app: App) {
        if (selectedAppForDrag != null){
            if (app.packageName == "") {
                app.name = selectedAppForDrag!!.name
                app.icon = selectedAppForDrag!!.icon
                app.packageName = selectedAppForDrag!!.packageName
                appDraggedLabel.text = ""
                draggedAppNotifyLayout.visibility = View.GONE

                if (screen == HOME_SCREEN) {
                    selectedAppForDrag= null
                    Toast.makeText(this, "move it", Toast.LENGTH_SHORT).show()
                }
                selectedAppForDrag = App("", "", resources.getDrawable(R.drawable.empty_icon))
                viewPagerHomeAdapter.notifyAppHomeChange()
            } else Toast.makeText(this, "Cell is occupied select another cell!", Toast.LENGTH_SHORT).show()
        } else openApp(app)

        screen = HOME_SCREEN
    }


    private fun getInstalledAppList(): ArrayList<App> {
        val installedApps = ArrayList<App>()
        // get the apps from android phone
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val apps = applicationContext.packageManager.queryIntentActivities(intent, 0) as List<ResolveInfo>
        for (app in apps) {
            val appName = app.activityInfo.loadLabel(packageManager).toString()
            val appIcon = app.activityInfo.loadIcon(packageManager)
            val packageName = app.activityInfo.packageName
            val installedApp = App(packageName, appName, appIcon)
            if (!(installedApp in installedApps)) installedApps.add(installedApp)
        }

        return installedApps;
    }

    fun appHomeLongClicked(app: App) {
        selectedApp(app, HOME_SCREEN)
        //app =
    }


    private fun selectedApp(app: App, screenState: String) {
        if (app.packageName.isNullOrEmpty()) return
        // add selected app
        selectedAppForDrag = App(app.packageName, app.name, app.icon)

        draggedAppNotifyLayout.visibility = View.VISIBLE
        appDraggedLabel.text = selectedAppForDrag!!.name
        screen = screenState
    }

    fun unSelectAppClicked(v: View) {
        selectedAppForDrag = null
        draggedAppNotifyLayout.visibility = View.GONE
        appDraggedLabel.text = ""
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.luncher_options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.settings_option -> {
                val toSettings = Intent(this, SettingsActivity::class.java)
                startActivity(toSettings)
            }

            R.id.help_option -> {}
            R.id.wallpaper_option -> {
                val toWallpaperSettings = Intent(this, WallpaperSettingsActivity::class.java)
                startActivity(toWallpaperSettings)
            }
        }

        return true
    }

    fun deleteSelectedAppClicked(view: View) {
        val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE)
        intent.data = Uri.parse("package:${selectedAppForDrag?.packageName}")
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
        startActivityForResult(intent, 2020)
        draggedAppNotifyLayout.visibility = View.GONE
        appDraggedLabel.text = ""
    }


    fun getDisplayContentHeight(): Int {
        var actionBarHeight = 0; var statusBarHeight = 0; var contentTop = 0; var fullScreenHeight = 0;
        if (getActionBar() != null) actionBarHeight = getActionBar().height
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) statusBarHeight = resources.getDimensionPixelSize(resourceId)
        contentTop = (findViewById<View>(android.R.id.content)).getTop();

        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        fullScreenHeight = size.y

        return  fullScreenHeight - contentTop - statusBarHeight - actionBarHeight
    }

    override fun onResume() {
        // note: not optimized way
        setupPrefs()
        initializeHome()
        super.onResume()
    }


    fun setupPrefs() {
        preferences = Preferences(this)
        if (!preferences.wallpaper.isNullOrBlank()) launcherHomeScreenWallpaper.setImageURI(Uri.parse(preferences.wallpaper))
        numColumns_home = preferences.home_columns + COLS_ROWS_SEEK_BAR_SHIFT
        numRow_home     = preferences.home_rows + COLS_ROWS_SEEK_BAR_SHIFT
    }

    fun openApp(app: App) {
        if (!app.packageName.isNullOrEmpty()) {
            val openApp = this.packageManager.getLaunchIntentForPackage(app.packageName)
            startActivity(openApp)
        }
    }




} // Class End


