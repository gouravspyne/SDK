package com.spyneai.shootlibrary.shoot.holders

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.spyneai.shootlibrary.BaseApplication
import com.spyneai.shootlibrary.GenericAdapter
import com.spyneai.shootlibrary.OnItemClickListener
import com.spyneai.shootlibrary.R
import com.spyneai.shootlibrary.shoot.data.OnOverlaySelectionListener
import com.spyneai.shootlibrary.shoot.data.model.NewSubCatResponse

class InteriorHolder(
    itemView: View,
    listener: OnItemClickListener?,
    overlaySelectionListener : OnOverlaySelectionListener?
) : RecyclerView.ViewHolder(itemView), GenericAdapter.Binder<NewSubCatResponse.Interior>{

    var listener: OnItemClickListener? = null
    var overlaySelectionListener: OnOverlaySelectionListener? = null
    var binding : ItemInteriorBinding? = null
    val TAG = "OverlaysHolder"

    init {
        binding = ItemInteriorBinding.bind(itemView)
        this.listener = listener
        this.overlaySelectionListener = overlaySelectionListener
    }

    override fun bind(data: NewSubCatResponse.Interior) {
        //set sequence number as per adapter position
       // data.sequenceNumber = adapterPosition

        binding.apply {
            this?.tvName?.text = data.display_name
        }

        when{
            data.isSelected -> {
                binding?.flOverlay?.background = ContextCompat.getDrawable(BaseApplication.getContext(),R.drawable.bg_overlay_selected)
                overlaySelectionListener?.onOverlaySelected(
                    binding?.flOverlay!!,
                    adapterPosition,
                    data
                )
            }

            data.imageClicked -> {
                binding?.flOverlay?.background = ContextCompat.getDrawable(BaseApplication.getContext(),
                    R.drawable.bg_overlay_image_clicked)
            }

            else -> {
                binding?.flOverlay?.background = ContextCompat.getDrawable(BaseApplication.getContext(),R.drawable.bg_overlay)
            }
        }


        if (data.imageClicked){
            Log.d(TAG, "bind: "+data.imagePath)
            Glide.with(itemView)
                .load(data.imagePath)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding?.ivOverlay!!)
        }else {
            Glide.with(itemView)
                .load(data.display_thumbnail)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding?.ivOverlay!!)
        }

        binding?.flOverlay?.setOnClickListener {
            listener?.onItemClick(
                it,
                adapterPosition,
                data
            )
        }
    }
}