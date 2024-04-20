package com.ronny.scrollableimagegrid.data.remote

import com.ronny.scrollableimagegrid.data.dto.ImageData
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {

    @GET("media-coverages")
    suspend fun getImageList(
        @Query("limit") limit: Int
    ): ImageData
}