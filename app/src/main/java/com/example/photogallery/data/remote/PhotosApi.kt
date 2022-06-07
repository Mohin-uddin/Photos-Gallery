package com.example.photogallery.data.remote

import com.example.photogallery.data.model.photosList.PhotosListResponse
import com.example.photogallery.utils.ConstValue.API_KEY
import retrofit2.http.*

interface PhotosApi {


    @Headers("Authorization: Client-ID $API_KEY")
    @GET("photos")
    suspend fun getPhotosList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): PhotosListResponse
}