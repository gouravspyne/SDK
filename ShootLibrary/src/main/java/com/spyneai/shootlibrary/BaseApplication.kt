package com.spyneai.shootlibrary

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.Constraints
import androidx.work.NetworkType
import com.posthog.android.PostHog

@SuppressLint("StaticFieldLeak")
class BaseApplication : Application() {

    private val POSTHOG_API_KEY = "FoIzpWdbY_I9T_4jr5k4zzNuVJPcpzs_mIpO6y7581M"
    private val POSTHOG_HOST = "https://app.posthog.com"

    companion object {
        private lateinit var context: Context

        fun getContext(): Context {
            return context;
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this



//        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        //disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Create a PostHog client with the given context, API key and host.
        val posthog : PostHog = PostHog.Builder(this, POSTHOG_API_KEY, POSTHOG_HOST)
            .captureApplicationLifecycleEvents() // Record certain application events automatically!
            // .recordScreenViews() // Record screen views automatically!
            .build()


        // Set the initialized instance as a globally accessible instance.
        PostHog.setSingletonInstance(posthog)


        //process periodic worker
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

//        val longWorkRequest = PeriodicWorkRequestBuilder<ProcessSkuWorker>(
//            12, TimeUnit.HOURS)
//            .addTag("Periodic Processing Worker")
//
//        WorkManager.getInstance(context)
//            .enqueueUniquePeriodicWork(
//                "Process Unique",
//                ExistingPeriodicWorkPolicy.KEEP,
//                longWorkRequest
//                    .setConstraints(constraints)
//                    .build())

//        val repeatInternal = 30L
//        val flexInterval = 25L
//        val workerTag = "InternetWorker"
//
//        PeriodicWorkRequest
//            .Builder(InternetWorker::class.java, repeatInternal,
//                TimeUnit.MINUTES, flexInterval, TimeUnit.MINUTES)
//            .setConstraints(
//                Constraints.Builder()
//                    .setRequiredNetworkType(NetworkType.CONNECTED)
//                    .build())
//            .build()
//            .also {
//                WorkManager.getInstance(context).enqueueUniquePeriodicWork(workerTag, ExistingPeriodicWorkPolicy.REPLACE, it)
//            }

    }

}