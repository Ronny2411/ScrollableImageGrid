package com.ronny.scrollableimagegrid.domain.usecases

import androidx.paging.PagingData
import com.ronny.scrollableimagegrid.domain.model.ImageDataItem
import com.ronny.scrollableimagegrid.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow

class GetImageListUseCase(
    private val repository: ImageRepository
) {
    operator fun invoke(): Flow<PagingData<ImageDataItem>> {
        return repository.getImageList()
    }
}