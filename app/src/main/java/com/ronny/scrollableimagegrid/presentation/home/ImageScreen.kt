package com.ronny.scrollableimagegrid.presentation.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.ronny.scrollableimagegrid.R
import com.ronny.scrollableimagegrid.domain.model.ImageDataItem
import com.ronny.scrollableimagegrid.presentation.common.EmptyScreen
import com.ronny.scrollableimagegrid.presentation.common.ShimmerEffect
import com.ronny.scrollableimagegrid.ui.theme.EXTRA_SMALL_PADDING
import com.ronny.scrollableimagegrid.ui.theme.IMAGE_SIZE
import com.ronny.scrollableimagegrid.ui.theme.SMALL_PADDING
import com.ronny.scrollableimagegrid.ui.theme.ShimmerDarkGrey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ImageScreen(imageList: LazyPagingItems<ImageDataItem>) {
    val context = LocalContext.current
    val imageCache = remember { ImageCache(context) }
    val imageDataList by remember { mutableStateOf(imageList) }

    val result = handlePagingResult(image = imageList)
    if (result){
        LazyVerticalGrid(columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(all = SMALL_PADDING),
            verticalArrangement = Arrangement.spacedBy(EXTRA_SMALL_PADDING),
            horizontalArrangement = Arrangement.spacedBy(EXTRA_SMALL_PADDING),
            modifier = Modifier.background(ShimmerDarkGrey)) {
            items(imageDataList.itemCount) { index ->
                imageDataList[index]?.let { ImageItem(imageData = it, imageCache = imageCache) }
            }
        }
    }

}

@Composable
fun ImageItem(imageData: ImageDataItem, imageCache: ImageCache) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val imageUrl = imageData.thumbnail.domain + "/" + imageData.thumbnail.basePath + "/0/" + imageData.thumbnail.key
    LaunchedEffect(imageUrl) {
        bitmap = imageCache.getImage(imageUrl)
        if (bitmap == null) {
            val loadedBitmap = loadImageFromUrl(imageUrl)
            if (loadedBitmap != null) {
                imageCache.setImage(imageUrl, loadedBitmap)
                bitmap = loadedBitmap
            }
        }
    }

    if (bitmap==null){
        PlaceholderImage(modifier = Modifier.size(IMAGE_SIZE))
    } else {
        val resizedBitmap = bitmap?.let { resizeBitmap(it, 150, 150) }
        resizedBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(IMAGE_SIZE)
            )
        }
    }
}

suspend fun loadImageFromUrl(url: String): Bitmap? = withContext(Dispatchers.IO) {
    try {
        val connection = java.net.URL(url).openConnection()
        connection.connect()
        val inputStream = connection.getInputStream()
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun PlaceholderImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.placeholder),
        contentDescription = null,
        modifier = modifier
    )
}

fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val scaleWidth = maxWidth.toFloat() / width
    val scaleHeight = maxHeight.toFloat() / height
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
}

@Composable
fun handlePagingResult(
    image: LazyPagingItems<ImageDataItem>
): Boolean {
    val loadState = image.loadState
    val error = when{
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        else -> null
    }
    return when{
        loadState.refresh is LoadState.Loading->{
            ShimmerEffect()
            false
        }
        error!=null->{
            EmptyScreen(error = error)
            false
        }
        image.itemCount<1-> {
            EmptyScreen()
            false
        }
        else->true
    }
}