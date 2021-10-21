package com.spyneai.shootlibrary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spyneai.shoot.data.ProcessViewModel
import com.spyneai.shoot.data.ShootViewModel

class ViewModelFactory() : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(ShootViewModel::class.java) -> ShootViewModel() as T
            modelClass.isAssignableFrom(ProcessViewModel::class.java) -> ProcessViewModel() as T
            else -> throw IllegalArgumentException("ViewModelClass not found")
        }
    }

}