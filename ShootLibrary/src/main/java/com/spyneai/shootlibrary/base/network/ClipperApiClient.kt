package com.spyneai.shootlibrary.base.network


import com.spyneai.shootlibrary.needs.AppConstants

class  ClipperApiClient : BaseApiClient<ClipperApi>(AppConstants.BASE_URL, ClipperApi::class.java) {
}