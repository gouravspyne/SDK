package com.spyneai.shootlibrary

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.spyneai.shootlibrary.reshoot.ReshootHolder
import com.spyneai.shootlibrary.reshoot.SelectImageHolder
import com.spyneai.shootlibrary.shoot.data.OnOverlaySelectionListener
import com.spyneai.shootlibrary.shoot.holders.InteriorHolder
import com.spyneai.shootlibrary.shoot.holders.MiscHolder
import com.spyneai.shootlibrary.shoot.holders.OverlaysHolder
import com.spyneai.shootlibrary.shoot.holders.SubcategoryHolder


object JavaViewHolderFactory {

    fun create(view: View, viewType: Int, listener: OnItemClickListener,
               overlaySelectionListener: OnOverlaySelectionListener? = null): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_subcategories -> SubcategoryHolder(view, listener)
            R.layout.item_overlays -> OverlaysHolder(view, listener,overlaySelectionListener)
            R.layout.item_interior -> InteriorHolder(view, listener,overlaySelectionListener)
            R.layout.item_miscellanous -> MiscHolder(view, listener,overlaySelectionListener)
            R.layout.item_select_image -> SelectImageHolder(view, listener)
            R.layout.item_reshoot -> ReshootHolder(view,listener,overlaySelectionListener)

            else -> GenericViewHolder(view)
        }
    }


}