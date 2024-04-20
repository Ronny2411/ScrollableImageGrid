package com.ronny.scrollableimagegrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.ronny.scrollableimagegrid.presentation.home.ImageScreen
import com.ronny.scrollableimagegrid.presentation.home.ImageViewModel
import com.ronny.scrollableimagegrid.ui.theme.ScrollableImageGridTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScrollableImageGridTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: ImageViewModel = hiltViewModel()
                    val imageList = viewModel.imageList.collectAsLazyPagingItems()
                    ImageScreen(imageList)
                }
            }
        }
    }
}