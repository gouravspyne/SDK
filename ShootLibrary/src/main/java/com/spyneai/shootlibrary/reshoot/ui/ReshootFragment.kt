package com.spyneai.shootlibrary.reshoot.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.snackbar.Snackbar
import com.posthog.android.Properties
import com.spyneai.reshoot.ReshootAdapter
import com.spyneai.shoot.ui.dialogs.ConfirmReshootDialog
import com.spyneai.shoot.ui.dialogs.ConfirmTagsDialog
import com.spyneai.shoot.ui.dialogs.ReclickDialog
import com.spyneai.shoot.utils.shoot
import com.spyneai.shootlibrary.BaseFragment
import com.spyneai.shootlibrary.OnItemClickListener
import com.spyneai.shootlibrary.R
import com.spyneai.shootlibrary.needs.AppConstants
import com.spyneai.shootlibrary.needs.Utilities
import com.spyneai.shootlibrary.posthog.Events
import com.spyneai.shootlibrary.reshoot.data.ReshootOverlaysRes
import com.spyneai.shootlibrary.reshoot.data.SelectedImagesHelper
import com.spyneai.shootlibrary.shoot.data.OnOverlaySelectionListener
import com.spyneai.shootlibrary.shoot.data.ShootViewModel
import com.spyneai.shootlibrary.shoot.data.model.ShootData
import org.json.JSONArray

class ReshootFragment : BaseFragment<ShootViewModel, FragmentReshootBinding>(), OnItemClickListener,
    OnOverlaySelectionListener {

    var reshootAdapter: ReshootAdapter? = null
    var snackbar : Snackbar? = null
    val TAG = "ReshootFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getOverlayIds()
        observerOverlayIds()

        binding.apply {
            tvSkuName.text = viewModel.sku.value?.skuName
        }
        //observe new image clicked
        viewModel.shootList.observe(viewLifecycleOwner, {
            try {
                if (viewModel.showConfirmReshootDialog.value == true && !it.isNullOrEmpty()) {
                    val element = viewModel.getCurrentShoot()
                    showImageConfirmDialog(element!!)
                }
            } catch (e: Exception) {
                Log.d(TAG, "onViewCreated: " + e.localizedMessage)
                e.printStackTrace()
            }
        })

        viewModel.onImageConfirmed.observe(viewLifecycleOwner, {
            if (viewModel.shootList.value != null) {
                val list = reshootAdapter?.listItems as List<ReshootOverlaysRes.Data>

                val position = viewModel.currentShoot

                list[position].isSelected = false
                list[position].imageClicked = true
                list[position].imagePath = viewModel.getCurrentShoot()!!.capturedImage
                reshootAdapter?.notifyItemChanged(position)

                if (position != list.size.minus(1)) {
                    var foundNext = false

                    for (i in position..list.size.minus(1)) {
                        if (!list[i].isSelected && !list[i].imageClicked) {
                            foundNext = true
                            list[i].isSelected = true
                            reshootAdapter?.notifyItemChanged(i)
                            binding.rvImages.scrollToPosition(i.plus(2))
                            break
                        }
                    }

                    if (!foundNext) {
                        val element = list.firstOrNull {
                            !it.isSelected && !it.imageClicked
                        }

                        if (element != null) {
                            element?.isSelected = true
                            reshootAdapter?.notifyItemChanged(list.indexOf(element))
                            binding.rvImages.scrollToPosition(viewModel.currentShoot)
                        }
                    }
                } else {
                    val element = list.firstOrNull {
                        !it.isSelected && !it.imageClicked
                    }

                    if (element != null) {
                        element?.isSelected = true
                        reshootAdapter?.notifyItemChanged(list.indexOf(element))
                        binding.rvImages.scrollToPosition(viewModel.currentShoot)
                    }
                }

                val reshootList = reshootAdapter?.listItems as List<ReshootOverlaysRes.Data>
                viewModel.allReshootClicked = reshootList.all { it.imageClicked }
            }
        })

        viewModel.isCameraButtonClickable = true

    }

    private fun getOverlayIds() {
        Utilities.showProgressDialog(requireContext())

        val ids = JSONArray()

        SelectedImagesHelper.selectedImages.keys.forEach {
            ids.put(it)
        }
        viewModel.getOverlayIds(ids)
    }

    private fun observerOverlayIds() {
        viewModel.reshootOverlaysRes.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    Utilities.hideProgressDialog()
                    val list = it.value.data
                    var index = 0

                    list.forEach {
                        it.imageName = SelectedImagesHelper.selectedImages.get(it.id).toString()
                    }

                    if (viewModel.shootList.value != null){
                        list.forEach { overlay ->
                            val element = viewModel.shootList.value!!.firstOrNull {
                                it.overlayId == overlay.id
                            }

                            if (element != null){
                                overlay.imageClicked = true
                                overlay.imagePath = element.capturedImage
                            }
                        }

                        val element = list.first {
                            !it.isSelected && !it.imageClicked
                        }

                        element.isSelected = true
                        index = list.indexOf(element)

                    }else{
                        //set overlays
                        list[index].isSelected = true
                    }

                    //set recycler view
                    reshootAdapter = ReshootAdapter(
                        list,
                        this,
                        this
                    )

                    binding.rvImages.apply {
                        layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        adapter = reshootAdapter
                        scrollToPosition(index)
                    }
                }

                is Resource.Failure -> {
                    Utilities.hideProgressDialog()
                    handleApiError(it) { getOverlayIds() }
                }
            }
        })
    }

    private fun showImageConfirmDialog(shootData: ShootData) {
        viewModel.shootData.value = shootData
        when (getString(R.string.app_name)) {
            AppConstants.OLA_CABS -> {
                ConfirmTagsDialog().show(
                    requireActivity().supportFragmentManager,
                    "ConfirmTagsDialog"
                )
            }
            else -> {
                ConfirmReshootDialog().show(
                    requireActivity().supportFragmentManager,
                    "ConfirmReshootDialog"
                )
            }
        }


    }

    override fun onItemClick(view: View, position: Int, data: Any?) {
        when (data) {
            is ReshootOverlaysRes.Data -> {

                if (data.imageClicked){
                    ReclickDialog().show(requireActivity().supportFragmentManager,"ReclickDialog")
                }
                val list = reshootAdapter?.listItems as List<ReshootOverlaysRes.Data>

                val element = list.firstOrNull {
                    it.isSelected
                }

                if (element != null && data != element) {
                    //loadOverlay(data.angle_name,data.display_thumbnail)
                    //viewModel.selectedOverlay = data

                    data.isSelected = true
                    element.isSelected = false
                    reshootAdapter?.notifyItemChanged(position)
                    reshootAdapter?.notifyItemChanged(list.indexOf(element))
                    binding.rvImages.scrollToPosition(position)
                }
            }
        }
    }

    override fun onOverlaySelected(view: View, position: Int, data: Any?) {
        viewModel.currentShoot = position

        when (data) {
            is ReshootOverlaysRes.Data -> {
                viewModel.reshotImageName = data.imageName

                if (data.type == "Exterior"){
                    viewModel.showLeveler.value = true
                    binding.imgOverlay.visibility = View.VISIBLE
                    loadOverlay(data.displayName,data.displayThumbnail)
                }else {
                    viewModel.hideLeveler.value = true
                    binding.imgOverlay.visibility = View.GONE
                }

                if (getString(R.string.app_name) == AppConstants.KARVI)
                    binding.imgOverlay.visibility = View.GONE

                viewModel.categoryDetails.value?.imageType = data.type

                viewModel.overlayId = data.id

                binding.tvShoot?.text = "Angles ${position.plus(1)}/${SelectedImagesHelper.selectedImages.size}"
            }

        }
    }

    private fun loadOverlay(name : String,overlay : String) {

        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .signature(ObjectKey(overlay))

        Glide.with(requireContext())
            .load(overlay)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    val properties =  Properties()
                    properties["name"] = name
                    properties["error"] = e?.localizedMessage
                    properties["category"] = viewModel.categoryDetails.value?.categoryName

                    requireContext().captureEvent(
                        Events.OVERLAY_LOAD_FIALED,
                        properties
                    )

                    snackbar = Snackbar.make(binding.root, "Overlay Failed to load", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry") {
                            loadOverlay(name,overlay)
                        }
                        .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.primary))

                    snackbar?.show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {

                    if (snackbar != null)
                        snackbar?.dismiss()

                    val properties =  Properties()
                    properties["name"] = name
                    properties["category"] = viewModel.categoryDetails.value?.categoryName

                    requireContext().captureEvent(
                        Events.OVERLAY_LOADED,
                        properties
                    )

                    getPreviewDimensions(binding.imgOverlay!!)
                    return false
                }

            })
            .apply(requestOptions)
            .into(binding.imgOverlay!!)

    }

    private fun getPreviewDimensions(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val shootDimensions = viewModel.shootDimensions.value
                shootDimensions?.overlayWidth = view.width
                shootDimensions?.overlayHeight = view.height

                viewModel.shootDimensions.value = shootDimensions
            }
        })
    }

    override fun getViewModel() = ShootViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentReshootBinding.inflate(inflater, container, false)

}