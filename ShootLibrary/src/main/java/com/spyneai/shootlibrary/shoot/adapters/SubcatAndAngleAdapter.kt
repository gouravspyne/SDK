package com.spyneai.shootlibrary.shoot.adapters

import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.spyneai.dashboard.response.NewSubCatResponse
import com.spyneai.shootlibrary.GenericAdapter
import com.spyneai.shootlibrary.JavaViewHolderFactory
import com.spyneai.shootlibrary.R

class SubcatAndAngleAdapter(
    list: List<Any>,
    var listener: AdapterView.OnItemClickListener
) : GenericAdapter<Any>(list) {

    override fun getLayoutId(position: Int, obj: Any?): Int {
        return when (obj) {

            is NewSubCatResponse.Data -> R.layout.item_subcategories
            else -> error("Unknown type: for position: $position")
        }
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return JavaViewHolderFactory.create(view, viewType, listener)
    }


}