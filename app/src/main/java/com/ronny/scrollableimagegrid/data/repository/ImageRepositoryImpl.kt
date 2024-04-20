package com.ronny.scrollableimagegrid.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ronny.scrollableimagegrid.data.remote.ImageApi
import com.ronny.scrollableimagegrid.data.remote.ImageListPagingSource
import com.ronny.scrollableimagegrid.domain.model.ImageDataItem
import com.ronny.scrollableimagegrid.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow

class ImageRepositoryImpl(
    private val imageApi: ImageApi
): ImageRepository {

    override fun getImageList(): Flow<PagingData<ImageDataItem>> {
        return Pager(
            config = PagingConfig(pageSize = 15),
            pagingSourceFactory = {
                ImageListPagingSource(
                    imageApi = imageApi
                )
            }
        ).flow
    }

}