package com.balsikandar.kotlindslsamples.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class MyDialogFragment : DialogFragment() {

    var layoutId: Int? = null

    lateinit var callback: (View, DialogFragment) -> Unit

    companion object {


        fun newInstance(layoutId: Int?): MyDialogFragment {

            val frag = MyDialogFragment()
            frag.setArguments(Bundle().apply {
                putInt("layoutId", layoutId!!)
            })
            return frag
        }
    }

    fun setCustomView(customView: (View, DialogFragment) -> Unit) {
        callback = customView
    }

    override fun onStart() {
        super.onStart()
        val deviceWidth = requireContext().resources.displayMetrics.widthPixels
        dialog?.window?.setLayout(deviceWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutId = arguments?.getInt("layoutId")
        if (layoutId == null) throw Exception("layoutId can't be null")
        return inflater.inflate(layoutId!!, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback.invoke(view, this)
    }

}