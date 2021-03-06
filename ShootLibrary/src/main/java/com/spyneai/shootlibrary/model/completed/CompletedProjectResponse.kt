package com.spyneai.model.projects

import com.google.gson.annotations.SerializedName

data class CompletedProjectResponse(
    @SerializedName("total_frames")
    val total_frames: String,
    @SerializedName("current_frame")
    val current_frame: String,
    @SerializedName("output_image_url")
    val output_image_url: String,
    @SerializedName("sku_id")
    val sku_id: String,
    @SerializedName("sku_name"CompletedProjectResponse)
    val sku_name: String,
    @SerializedName("created_at")
    val created_at: String,

)
