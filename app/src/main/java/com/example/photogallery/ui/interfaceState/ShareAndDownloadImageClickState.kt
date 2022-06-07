package com.example.photogallery.ui.interfaceState



interface ShareAndDownloadImageClickState {
    fun downloadImage(url: String)
    fun shareImage(url: String)
    fun zoomImage(url: String)
}