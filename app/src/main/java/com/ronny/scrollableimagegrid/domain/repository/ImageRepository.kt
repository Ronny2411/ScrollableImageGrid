package com.ronny.scrollableimagegrid.domain.repository

import androidx.paging.PagingData
import com.ronny.scrollableimagegrid.domain.model.ImageDataItem
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    fun getImageList(): Flow<PagingData<ImageDataItem>>

}