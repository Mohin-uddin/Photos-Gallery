package com.example.photogallery.di


import com.example.photogallery.data.remote.PhotosApi
import com.example.photogallery.repository.photosList.PhotosListRepository
import com.example.photogallery.repository.photosList.PhotosListRepositoryImp
import com.example.photogallery.utils.ConstValue.BASE_URL
import com.example.photogallery.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun providePhotosRepository(
        api : PhotosApi
    ):PhotosListRepository = PhotosListRepositoryImp(api)


    @Singleton
    @Provides
    fun providePhotosApi(): PhotosApi = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create()

    @Singleton
    @Provides
    fun provideDispatcher() : DispatcherProvider = object : DispatcherProvider{
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }

}