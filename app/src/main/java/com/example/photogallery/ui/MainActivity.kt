package com.example.photogallery.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photogallery.R
import com.example.photogallery.data.model.photosList.PhotosListResponse
import com.example.photogallery.data.model.photosList.PhotosListResponseItem
import com.example.photogallery.databinding.ActivityMainBinding
import com.example.photogallery.ui.adapter.PhotosAdapter
import com.example.photogallery.ui.interfaceState.ShareAndDownloadImageClickState
import com.example.photogallery.ui.pictureDetails.PictureDetailsActivity
import com.example.photogallery.utils.InternetConnectivityCheck
import com.example.photogallery.utils.changeVisibilityStatus
import com.example.photogallery.utils.gone
import com.example.photogallery.utils.show
import com.example.photogallery.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ShareAndDownloadImageClickState {
    private lateinit var layoutManager: GridLayoutManager
    lateinit var photosAdapter : PhotosAdapter
    private lateinit var binding: ActivityMainBinding
     var listOfPhotos = ArrayList<PhotosListResponseItem>()
    val viewModel : MainViewModel by viewModels()
    private var dataLoadingState : Boolean = false
    private var lastPageState : Boolean = false
    private var page = 1
    lateinit var connectionLiveData : InternetConnectivityCheck
    private var state =0
    private var networkState = false
    private var pandingDownloadUrl=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        connectionLiveData = InternetConnectivityCheck(this)
        connectionLiveData.observe(this) { isNetworkState ->

            if (isNetworkState) {
                if(state==1) {

                    lifecycleScope.launch {
                        binding.tvNetworkState.text = "Internet connected"
                        binding.tvNetworkState.setTextColor(getColor(R.color.white))
                        binding.tvNetworkState.setBackgroundColor(getColor(R.color.purple_700))
                        delay(2000)
                        binding.tvNetworkState.changeVisibilityStatus()
                        if(pandingDownloadUrl.isNotEmpty()) {
                            Log.e("checkPandingDownload", "onCreate: $pandingDownloadUrl" )
                            viewModel.imageDownload(pandingDownloadUrl, this@MainActivity)
                            pandingDownloadUrl=""
                        }
                    }
                    state=0
                }
                getDataCall()
            } else {
                state=1
                binding.tvNetworkState.changeVisibilityStatus()
                binding.tvNetworkState.text = "Internet connection lost"
                binding.tvNetworkState.setTextColor(getColor(R.color.black))
                binding.tvNetworkState.setBackgroundColor(getColor(R.color.purple_200))
            }
            networkState=isNetworkState
        }

        adapterInitialize()
        recycleViewScrollState()
    }

    private fun recycleViewScrollState() {
        binding.rvPhotos.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val currentItem = layoutManager.childCount
                val firstPos =layoutManager.findFirstVisibleItemPosition()
                if(!dataLoadingState && !lastPageState){

                    if(firstPos+currentItem>=25&&firstPos/30<=page){
                        page++
                        viewModel.getPhotosListRequest(page)
                    }
                }

            }
        })
    }

    private fun getDataCall() {
        dataLoadingState =true
        viewModel.getPhotosListRequest(page)
        lifecycleScope.launchWhenCreated {

            viewModel.photosListResponse.collect { value: PhotosListResponse? ->

                binding.tvLoading.gone()
                binding.rvPhotos.show()
                if (value?.isNotEmpty() == true){
                    listOfPhotos.addAll(value)
                    photosAdapter.setUpdateData(listOfPhotos)
                    lastPageState = if(0<value.size){
                        value.size < 30
                    } else true


                }
                dataLoadingState=false
            }
        }
    }

    private fun adapterInitialize() {
        photosAdapter = PhotosAdapter(this)
        layoutManager = GridLayoutManager(applicationContext,2)
        binding.rvPhotos.layoutManager = layoutManager
        binding.rvPhotos.adapter = photosAdapter

    }

    override fun downloadImage(url: String) {
        try {


            if (isStoragePermissionGranted())
            {
                if(networkState) {
                    viewModel.imageDownload(url, this)
                }
                else{
                    pandingDownloadUrl=url
                    Toast.makeText(this@MainActivity,"Please connect network",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this,"Please allow to all permission",Toast.LENGTH_LONG).show()
            }

        } catch (e: Exception) {
            Log.e("cheacking", e.toString())
        }
    }

    override fun shareImage(url: String) {
        if(url.isNotEmpty()) {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, url)
            sendIntent.type = "text/plain"
            Intent.createChooser(sendIntent, "Share via")
            startActivity(sendIntent)
        }else{
            Toast.makeText(this, "Please select another picture",Toast.LENGTH_LONG).show()
        }
    }

    override fun zoomImage(url: String) {
        val intent = Intent(this@MainActivity, PictureDetailsActivity::class.java)
        intent.putExtra("image_url", url)
        startActivity(intent)
    }

    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else {
            true
        }
    }

}