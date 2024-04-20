package com.ronny.scrollableimagegrid.presentation.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.ronny.scrollableimagegrid.R
import com.ronny.scrollableimagegrid.domain.model.ImageDataItem
import com.ronny.scrollableimagegrid.ui.theme.IMAGE_PLACEHOLDER_SIZE
import com.ronny.scrollableimagegrid.ui.theme.SMALL_PADDING
import com.ronny.scrollableimagegrid.ui.theme.ShimmerDarkGrey
import com.ronny.scrollableimagegrid.ui.theme.ShimmerLightGrey
import java.net.ConnectException
import java.net.SocketTimeoutException

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EmptyScreen(error: LoadState.Error? = null,
                heroes: LazyPagingItems<ImageDataItem>? = null) {
    var message by remember{
        mutableStateOf("Something went wrong! Its not you, Its us")
    }
    var icon by remember {
        mutableStateOf(R.drawable.network_error)
    }
    if (error!=null){
        message = parseErrorMessage(error = error)
        icon = R.drawable.network_error
    }
    var startAnimation by remember {
        mutableStateOf(false)
    }
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) ContentAlpha.disabled else 0f,
        animationSpec = tween(
            durationMillis = 1000
        ), label = ""
    )
    LaunchedEffect(key1 = true){
        startAnimation = true
    }
    var isRefreshing by remember {
        mutableStateOf(false)
    }
    val refreshingState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            heroes?.refresh()
            isRefreshing = false
        })
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ShimmerDarkGrey)
            .pullRefresh(refreshingState, enabled = error != null)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(painter = painterResource(id = icon),
            contentDescription = stringResource(R.string.network_error_icon),
            modifier = Modifier
                .size(IMAGE_PLACEHOLDER_SIZE)
                .alpha(alphaAnim),
            tint = Color.White)
        Text(text = message,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            fontSize = MaterialTheme.typography.subtitle1.fontSize,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = SMALL_PADDING)
                .alpha(alphaAnim))
    }
}

fun parseErrorMessage(error: LoadState.Error): String{
    return when{
        error.error is SocketTimeoutException -> {
            "Server Unavailable"
        }
        error.error is ConnectException -> {
            "Internet Unavailable"
        }
        else -> {
            "Unknown Error"
        }
    }
}