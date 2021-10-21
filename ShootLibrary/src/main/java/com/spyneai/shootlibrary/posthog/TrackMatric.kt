package com.spyneai.shootlibrary.posthog

import android.content.Context
import com.posthog.android.PostHog
import com.posthog.android.Properties
import com.spyneai.shootlibrary.R
import com.spyneai.shootlibrary.needs.AppConstants
import com.spyneai.shootlibrary.needs.Utilities

fun Context.captureEvent(eventName : String, properties: Properties) {
//    properties.putValue("app_name",getString(R.string.app_name))

    PostHog.with(this)
        .capture(eventName, properties)
}

fun Context.captureFailureEvent(eventName : String,properties: Properties,message : String) {
    properties.putValue("message",message)
//    properties.putValue("app_name",getString(R.string.app_name))

    PostHog.with(this)
        .capture(eventName, properties)
}

fun Context.captureIdentity(userId : String,properties: Properties) {
    //save email
    Utilities.savePrefrence(this, AppConstants.EMAIL_ID,properties.getString("email"))
    Utilities.savePrefrence(this, AppConstants.TOKEN_ID, properties.getString("user_id"))
    Utilities.savePrefrence(this, AppConstants.USER_NAME, properties.getString("name"))
    Utilities.savePrefrence(this, AppConstants.USER_EMAIL, properties.getString("email"))
    
//    properties.putValue("app_name",getString(R.string.app_name))
    
    PostHog.with(this)
        .identify(userId,properties)

}