package com.example.photogallery.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.example.photogallery.R
import com.example.photogallery.data.model.photosList.PhotosListResponseItem
import com.example.photogallery.ui.interfaceState.ShareAndDownloadImageClickState
import com.example.photogallery.utils.PhotosDiffutils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_photos_details.*

class PhotosAdapter(val shareAndDownloadImageClickState: ShareAndDownloadImageClickState) : RecyclerView.Adapter<PhotosAdapter.PhotosHolder>() {

    private var oldPhotosList = emptyList<PhotosListResponseItem>()
    class PhotosHolder (itemView: View) : RecyclerView.ViewHolder(itemView),
        LayoutContainer {
        override val containerView: View
            get() = itemView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return PhotosHolder(
            layoutInflater.inflate(
                R.layout.item_photos_details,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotosHolder, position: Int) {
        holder.ivGalleryPicture.load(oldPhotosList[position].urls.regular) {
            crossfade(true)
            crossfade(1000)
            placeholder(R.drawable.loading_2)
            transformations(RoundedCornersTransformation(30f))
        }
        holder.ivDownload.setOnClickListener {
            shareAndDownloadImageClickState.downloadImage(oldPhotosList[position].urls.regular)
        }
        holder.ivShare.setOnClickListener {
            shareAndDownloadImageClickState.shareImage(oldPhotosList[position].urls.regular)
        }
        holder.itemView.setOnClickListener {
          shareAndDownloadImageClickState.zoomImage(oldPhotosList[position].urls.regular)
        }

    }

    override fun getItemCount(): Int {
        return oldPhotosList.size
    }

    fun setUpdateData(newPhotosList : List<PhotosListResponseItem>){
        val diffUtil = PhotosDiffutils(oldPhotosList,newPhotosList)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        oldPhotosList = newPhotosList
        diffUtilResult.dispatchUpdatesTo(this)
    }


}