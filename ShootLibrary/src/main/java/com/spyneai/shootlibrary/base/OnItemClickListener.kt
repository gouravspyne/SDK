package com.spyneai.shootlibrary

import android.view.View

interface OnItemClickListener {
    fun onItemClick(view: View, position: Int = -1, data: Any? = null)
}