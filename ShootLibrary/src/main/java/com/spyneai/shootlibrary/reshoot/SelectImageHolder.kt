package com.spyneai.shootlibrary.reshoot

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.spyneai.shootlibrary.GenericAdapter
import com.spyneai.shootlibrary.OnItemClickListener


class SelectImageHolder(
    itemView: View,
    listener: OnItemClickListener?)
    : RecyclerView.ViewHolder(itemView), GenericAdapter.Binder<ImagesOfSkuRes.Data>{

    var listener: OnItemClickListener? = null
    var binding : ItemSelectImageBinding? = null
    val TAG = "ReshootHolder"

    init {
        binding = ItemSelectImageBinding.bind(itemView)
        this.listener = listener
    }

    override fun bind(data: ImagesOfSkuRes.Data) {

        var color = Integer.toHexString(
            ContextCompat.getColor(
                itemView.context,
                R.color.primary
            ) and 0x00ffffff
        )

        if (color.length == 5)
            color = "0"+color

        if (data.isSelected)
            binding?.clRoot?.setBackgroundColor(Color.parseColor("#36"+color))
        else
            binding?.clRoot?.setBackgroundColor(Color.parseColor("#1A878787"))

        binding?.cb?.isChecked= data.isSelected

        Glide.with(itemView)
            .load(data.input_image_lres_url)
            .into(binding?.ivBefore!!)

        Glide.with(itemView)
            .load(data.output_image_lres_url)
            .into(binding?.ivAfter!!)

        binding?.clRoot?.setOnClickListener {
            listener?.onItemClick(
                it,
                adapterPosition,
                data
            )
        }

    }
}