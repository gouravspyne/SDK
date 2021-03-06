package com.spyneai.shootlibrary.shoot.workmanager

import android.content.Context
import android.content.Intent
import android.media.MediaParser.SeekPoint.START
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.appindexing.builders.Actions
import com.posthog.android.Properties
import com.spyneai.shootlibrary.isMyServiceRunning
import com.spyneai.shootlibrary.posthog.Events
import com.spyneai.shootlibrary.shoot.data.ShootLocalRepository

class InternetWorker(private val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val shootLocalRepository = ShootLocalRepository()
        if (shootLocalRepository.getOldestImage().itemId != null
            || shootLocalRepository.getOldestSkippedImage().itemId != null){
            if (!appContext.isMyServiceRunning(ImageUploadingService::class.java)){
                capture(Events.SERVICE_STARTED,"Started")
                var action = Actions.START
                if (getServiceState(appContext) == com.spyneai.service.ServiceState.STOPPED && action == Actions.STOP)
                    return Result.success()

                val serviceIntent = Intent(appContext, ImageUploadingService::class.java)
                serviceIntent.action = action.name

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    log("Starting the service in >=26 Mode")
                    ContextCompat.startForegroundService(appContext, serviceIntent)
                    return Result.success()
                } else {
                    log("Starting the service in < 26 Mode")
                    appContext.startService(serviceIntent)
                }
            }else {
                capture(Events.SERVICE_STARTED,"Running")
            }
        }

        return Result.success()
    }

    private fun capture(eventName : String,state : String) {
        val prperties = Properties()
            .apply {
                put("service_state",state)
                put("email",Utilities.getPreference(appContext,AppConstants.EMAIL_ID).toString())
                put("medium","Internet Worker")
            }

        appContext.captureEvent(eventName,prperties)
    }
}