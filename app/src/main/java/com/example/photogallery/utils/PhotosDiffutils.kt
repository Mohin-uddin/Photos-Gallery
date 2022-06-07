package com.example.photogallery.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.photogallery.data.model.photosList.PhotosListResponseItem

class PhotosDiffutils(
    private val oldPhotosItem : List<PhotosListResponseItem>,
    private val newPhotosItem : List<PhotosListResponseItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
       return  oldPhotosItem.size
    }

    override fun getNewListSize(): Int {
        return newPhotosItem.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldPhotosItem[oldItemPosition].urls.regular == newPhotosItem[newItemPosition].urls.regular
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldPhotosItem == newPhotosItem
    }
}