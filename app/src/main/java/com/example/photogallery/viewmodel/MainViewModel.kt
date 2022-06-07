package com.example.photogallery.viewmodel

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photogallery.data.model.photosList.PhotosListResponse
import com.example.photogallery.repository.photosList.PhotosListRepository
import com.example.photogallery.utils.DispatcherProvider
import com.example.photogallery.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import android.net.NetworkInfo

import androidx.core.content.ContextCompat.getSystemService

import android.net.ConnectivityManager
import android.widget.Toast
import androidx.core.content.ContextCompat


@HiltViewModel
class MainViewModel @Inject constructor(
    private val photoRepository: PhotosListRepository,
    private var dispatcher: DispatcherProvider
): ViewModel() {

    private var _photosListResponse = MutableStateFlow<PhotosListResponse?>(null)
    var photosListResponse: StateFlow<PhotosListResponse?> = _photosListResponse.asStateFlow()

    fun getPhotosListRequest( page :Int){
        viewModelScope.launch(dispatcher.io) {
            val result = photoRepository.getPhotosList(page,30)

            when(result){
                is Resource.Success -> {
                    //_responseCode.emit(result.data?.code.toString())
                    _photosListResponse.emit(result.data)
                    Log.e("dataCheck", "getLoginRequest: asd "+result.message )
                }
                is Resource.Error -> {
                    Log.e("dataCheck", "getLoginRequest:aida ni "+result.message )
                  //  _responseCode.emit(result.message.toString())
                    Log.e("dataCheck", "getLoginRequest:aida pore "+result.message )
                }
            }
        }
    }


    fun imageDownload(url: String, context: Context){

        val sdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val currentDateandTime: String = sdf.format(Date())
        //Log.e("timeCurrent", "imageDownload: $currentDateandTime" )
        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)
        request.setTitle("downloading")
        request.setAllowedOverRoaming(false)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setDescription("Downloading a file")
        request.addRequestHeader("Accept", "image/jpeg")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$currentDateandTime.jpeg");
        val downloadManager = context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

}