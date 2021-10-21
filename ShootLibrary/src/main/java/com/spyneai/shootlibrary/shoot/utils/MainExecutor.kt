package com.spyneai.shootlibrary.shoot.utils


import android.os.Handler
import android.os.Looper
import com.spyneai.shoot.utils.ThreadExecutor

class MainExecutor : ThreadExecutor(Handler(Looper.getMainLooper())) {
    override fun execute(runnable: Runnable) {
        handler.post(runnable)
    }
}
