package com.ronny.scrollableimagegrid.di

import com.ronny.scrollableimagegrid.data.remote.ImageApi
import com.ronny.scrollableimagegrid.data.repository.ImageRepositoryImpl
import com.ronny.scrollableimagegrid.domain.repository.ImageRepository
import com.ronny.scrollableimagegrid.domain.usecases.GetImageListUseCase
import com.ronny.scrollableimagegrid.domain.usecases.ImageUseCases
import com.ronny.scrollableimagegrid.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesGameApi(): ImageApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImageApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGameRepository(
        imageApi: ImageApi,
    ): ImageRepository = ImageRepositoryImpl(imageApi = imageApi)

    @Provides
    @Singleton
    fun provideGameUseCases(
        imageRepository: ImageRepository
    ) = ImageUseCases(
        getImageListUseCase = GetImageListUseCase(repository = imageRepository),
    )
}