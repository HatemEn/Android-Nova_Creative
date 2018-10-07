package com.me.hatem.a09_nov_creative.Service

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.view.DragEvent
import android.view.View
import android.widget.Toast
import com.me.hatem.a09_nov_creative.Utilises.DRAGGED_APP_PACKAGE_NAME
import kotlinx.android.synthetic.main.item_app.view.*

object DragAndDropService {

    fun startDrag( view: View) {
        val intent = Intent()
        intent.putExtra(DRAGGED_APP_PACKAGE_NAME, view.appPackageName.tag as String)
        val item = ClipData.Item(intent)

        val dragData = ClipData(
                view.tag as? CharSequence,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item)

        val myShadow = View.DragShadowBuilder(view)

        // Starts the drag
        view.startDrag(
                dragData,   // the data to be dragged
                myShadow,   // the drag shadow builder
                null,       // no need to use local data
                0           // flags (not currently used, set to 0)
        )
    }



    fun dragListener(context: Context, view: View, dragEvent: DragEvent) {
        when(dragEvent.action) {
            DragEvent.ACTION_DRAG_ENTERED -> {}
            DragEvent.ACTION_DRAG_EXITED -> {}
            DragEvent.ACTION_DROP -> {
                // Gets the item containing the dragged data
                val item: ClipData.Item = dragEvent.clipData.getItemAt(0)

                // Gets the text data from the item.
                val dragAppPackageName = item.intent.getStringExtra(DRAGGED_APP_PACKAGE_NAME)
                Toast.makeText(context, dragAppPackageName+ " $ "+ view.appPackageName.tag, Toast.LENGTH_SHORT).show()
                AppsService.putAppOnHome(context, view, dragAppPackageName)
            }
        }
    }




}