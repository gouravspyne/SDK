package com.spyneai.shootlibrary.needs

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import android.util.Patterns
import android.view.Window
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.spyneai.shoot.utils.log
import com.spyneai.shootlibrary.R
import com.spyneai.shootlibrary.isValidGlideContext


object Utilities {


    private var dialog: Dialog? = null

    //Validating email id
    fun isValidEmail(email: String?): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }


    fun savePrefrence(context: Context, key: String, value: String?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getPreference(context: Context?, key: String): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(key, "")
    }

    fun showProgressDialog(context: Context) {
        log("show progress dialog")
        if (context.isValidGlideContext()) {
            dialog = Dialog(context!!)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setContentView(R.layout.dialog_progress)
            dialog!!.setCancelable(false)
            Glide.with(context).load(R.raw.loader).into(dialog!!.ivLoaders);
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.show()
        }

    }

    fun hideProgressDialog() {
        if (dialog != null) dialog!!.dismiss()
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }



    fun <T> setList(context: Context?, key: String?, list: List<T>?) {
        val gson = Gson()
        val json = gson.toJson(list)
        set(context, key, json)
    }

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    operator fun set(context: Context?, key: String?, value: String?) {
        sharedPreferences = context!!.getSharedPreferences(
            AppConstants.MY_LIST,
            Context.MODE_PRIVATE
        )
        editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()
    }

}