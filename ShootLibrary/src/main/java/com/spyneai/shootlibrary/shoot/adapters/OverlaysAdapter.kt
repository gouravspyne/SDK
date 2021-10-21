package com.spyneai.shootlibrary.shoot.adapters

import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.spyneai.dashboard.response.NewSubCatResponse
import com.spyneai.shoot.data.OnOverlaySelectionListener
import com.spyneai.shootlibrary.GenericAdapter
import com.spyneai.shootlibrary.JavaViewHolderFactory
import com.spyneai.shootlibrary.R
import com.spyneai.shootlibrary.camera2.OverlaysResponse

class OverlaysAdapter (
    list: List<Any>,
    var listener: AdapterView.OnItemClickListener,
    var overlaySelectionListener : OnOverlaySelectionListener
) : GenericAdapter<Any>(list) {

    override fun getLayoutId(position: Int, obj: Any?): Int {
        return when (obj) {

            is OverlaysResponse.Data -> R.layout.item_overlays
            is NewSubCatResponse.Interior -> R.layout.item_interior
            is NewSubCatResponse.Miscellaneous -> R.layout.item_miscellanous
            else -> error("Unknown type: for position: $position")
        }
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return JavaViewHolderFactory.create(view, viewType, listener,overlaySelectionListener)
    }


}