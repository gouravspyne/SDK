package com.spyneai.shootlibrary.shoot.utils

import android.content.Context

import android.os.Build
import com.spyneai.shoot.utils.MainExecutor

import java.util.concurrent.Executor


fun Context.mainExecutor(): Executor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
    mainExecutor
} else {
    MainExecutor()
}
