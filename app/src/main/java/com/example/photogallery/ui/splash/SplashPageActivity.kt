package com.example.photogallery.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import com.example.photogallery.R
import com.example.photogallery.databinding.ActivitySplashPageBinding
import com.example.photogallery.ui.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashPageActivity : AppCompatActivity() {
    lateinit var binding : ActivitySplashPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val hyperspaceJump: Animation = AnimationUtils.loadAnimation(this, R.anim.fadein)
        binding.ivSplashLogo.startAnimation(hyperspaceJump)
        lifecycleScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashPageActivity,MainActivity::class.java))
            finish()

        }
    }
}