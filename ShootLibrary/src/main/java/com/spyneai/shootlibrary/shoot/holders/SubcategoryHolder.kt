package com.spyneai.shootlibrary.shoot.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.spyneai.shootlibrary.GenericAdapter
import com.spyneai.shootlibrary.OnItemClickListener
import com.spyneai.shootlibrary.needs.AppConstants
import com.spyneai.shootlibrary.shoot.data.model.NewSubCatResponse

class SubcategoryHolder(
    itemView: View,
    listener: OnItemClickListener?
) : RecyclerView.ViewHolder(itemView), GenericAdapter.Binder<NewSubCatResponse.Data> {

    var listener: OnItemClickListener? = null
    var binding : ItemSubcategoriesBinding? = null

    init {
        binding = ItemSubcategoriesBinding.bind(itemView)
        this.listener = listener
    }

    override fun bind(data: NewSubCatResponse.Data) {

        binding.apply {
            this?.tvSubcategories?.text = data.sub_cat_name
        }

        Glide.with(itemView)
            .load(AppConstants.BASE_IMAGE_URL + data.display_thumbnail)
            .into(binding?.ivSubCategories!!)

        binding?.llSubCategories?.setOnClickListener {
            listener?.onItemClick(
                it,
                adapterPosition,
                data
            )
        }
    }
}