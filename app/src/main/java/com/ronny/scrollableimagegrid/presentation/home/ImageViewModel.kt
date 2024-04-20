package com.ronny.scrollableimagegrid.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ronny.scrollableimagegrid.domain.usecases.ImageUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val imageUseCases: ImageUseCases
): ViewModel() {
    var imageList = imageUseCases.getImageListUseCase().cachedIn(viewModelScope)
}