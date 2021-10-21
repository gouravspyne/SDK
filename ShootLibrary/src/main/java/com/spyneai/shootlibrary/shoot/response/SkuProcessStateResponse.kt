package com.spyneai.shootlibrary.shoot.response

import com.google.gson.annotations.SerializedName

data class SkuProcessStateResponse (
    @SerializedName("message") val message : String,
    @SerializedName("status") val status : Int,
    @SerializedName("background_id") val background_id : Int
        )