package com.spyneai.shootlibrary.reshoot

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.spyneai.shootlibrary.GenericAdapter
import com.spyneai.shootlibrary.JavaViewHolderFactory
import com.spyneai.shootlibrary.OnItemClickListener
import com.spyneai.shootlibrary.R
import com.spyneai.shootlibrary.reshoot.data.ReshootOverlaysRes
import com.spyneai.shootlibrary.shoot.data.OnOverlaySelectionListener

class ReshootAdapter(
    list: List<Any>,
    var listener: OnItemClickListener,
    var overlaySelectionListener : OnOverlaySelectionListener
) : GenericAdapter<Any>(list) {

    override fun getLayoutId(position: Int, obj: Any?): Int {
        return when (obj) {

            is ReshootOverlaysRes.Data -> R.layout.item_reshoot
            else -> error("Unknown type: for position: $position")
        }
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return JavaViewHolderFactory.create(view, viewType, listener,overlaySelectionListener)
    }


}