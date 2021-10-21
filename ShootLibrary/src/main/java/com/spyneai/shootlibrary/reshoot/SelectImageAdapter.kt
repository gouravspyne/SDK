package com.spyneai.shootlibrary.reshoot

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.spyneai.shootlibrary.GenericAdapter
import com.spyneai.shootlibrary.JavaViewHolderFactory
import com.spyneai.shootlibrary.OnItemClickListener
import com.spyneai.shootlibrary.R

class SelectImageAdapter(
    list: List<Any>,
    var listener: OnItemClickListener
) : GenericAdapter<Any>(list) {

    override fun getLayoutId(position: Int, obj: Any?): Int {
        return when (obj) {

            is ImagesOfSkuRes.Data -> R.layout.item_select_image
            else -> error("Unknown type: for position: $position")
        }
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return JavaViewHolderFactory.create(view, viewType, listener)
    }


}