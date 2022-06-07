package com.example.photogallery.repository.photosList

import com.example.photogallery.data.model.photosList.PhotosListResponse
import com.example.photogallery.utils.Resource

interface PhotosListRepository {
    suspend fun getPhotosList(page: Int, perPagePhotos: Int) : Resource<PhotosListResponse>
}