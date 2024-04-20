package com.ronny.scrollableimagegrid.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ronny.scrollableimagegrid.domain.model.ImageDataItem

class ImageListPagingSource(
    private val imageApi: ImageApi
): PagingSource<Int, ImageDataItem>() {

    private var totalImageCount = 0

    override fun getRefreshKey(state: PagingState<Int, ImageDataItem>): Int? {
        return state.anchorPosition?.let { anchorPosition->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageDataItem> {
        val page = params.key ?: 1
        return try {
            val imageResponse = imageApi.getImageList(
                limit = 100
            )
            totalImageCount+=imageResponse.size
            val images = imageResponse.distinctBy { it.title } //Remove Duplicate
            LoadResult.Page(
                data = images,
                nextKey = if (totalImageCount == imageResponse.size) null else page + 1,
                prevKey = null
            )
        } catch (e: Exception){
            e.printStackTrace()
            LoadResult.Error(
                throwable = e
            )
        }
    }
}