package com.example.photogallery.ui.pictureDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.example.photogallery.R
import com.example.photogallery.databinding.ActivityPictureDetailsBinding

class PictureDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPictureDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPictureDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val image_url = intent.getStringExtra("image_url")

        binding.ivBackPreviousPage.setOnClickListener {
            onBackPressed()
        }
        binding.zvPictureZoom.load(image_url) {
            crossfade(true)
            crossfade(1000)
            placeholder(R.drawable.loading_2)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}