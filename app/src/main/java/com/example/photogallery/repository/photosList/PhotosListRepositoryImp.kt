package com.example.photogallery.repository.photosList

import com.example.photogallery.data.model.photosList.PhotosListResponse
import com.example.photogallery.data.remote.PhotosApi
import com.example.photogallery.utils.Resource
import java.lang.Exception
import javax.inject.Inject

class PhotosListRepositoryImp @Inject constructor(
    private val api: PhotosApi
)  : PhotosListRepository {
    override suspend fun getPhotosList(
        page: Int,
        perPagePhotos: Int
    ): Resource<PhotosListResponse> {
        val response = try {
            api.getPhotosList(page,perPagePhotos)
        }catch (error: Exception){
            return Resource.Error("Something is wrong for this Api")
        }
        return Resource.Success(response)
    }
}