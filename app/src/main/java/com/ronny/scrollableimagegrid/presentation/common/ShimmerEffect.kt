package com.ronny.scrollableimagegrid.presentation.common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import com.ronny.scrollableimagegrid.ui.theme.EXTRA_SMALL_PADDING
import com.ronny.scrollableimagegrid.ui.theme.IMAGE_SIZE
import com.ronny.scrollableimagegrid.ui.theme.SMALL_PADDING
import com.ronny.scrollableimagegrid.ui.theme.ShimmerDarkGrey
import com.ronny.scrollableimagegrid.ui.theme.ShimmerMediumGrey

@Composable
fun ShimmerEffect() {
    LazyVerticalGrid(columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(all = SMALL_PADDING),
        verticalArrangement = Arrangement.spacedBy(SMALL_PADDING),
        horizontalArrangement = Arrangement.spacedBy(EXTRA_SMALL_PADDING),
        modifier = Modifier.background(ShimmerDarkGrey)
    ){
        items(count = 15){
            AnimateSimmerItem()
        }
    }
}

@Composable
fun AnimateSimmerItem() {
    val transition = rememberInfiniteTransition()
    val alphaAnim by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    ShimmerItem(alpha = alphaAnim)
}

@Composable
fun ShimmerItem(alpha: Float) {
    Surface(modifier = Modifier
        .size(IMAGE_SIZE)
        .fillMaxWidth(),
        shape = RoundedCornerShape(EXTRA_SMALL_PADDING)
    ) {
        Column(modifier = Modifier
            .padding(SMALL_PADDING),
            verticalArrangement = Arrangement.Center) {
            Surface(modifier = Modifier
                .alpha(alpha)
                .fillMaxWidth()
                .fillMaxHeight()
                .size(IMAGE_SIZE),
                color = ShimmerMediumGrey,
                shape = RoundedCornerShape(EXTRA_SMALL_PADDING)
            ) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShimmerItemPrev() {
    AnimateSimmerItem()
}