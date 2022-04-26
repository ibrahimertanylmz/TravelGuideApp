package com.turkcell.travelguideapp.view.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.turkcell.travelguideapp.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            binding.ConstraintLayoutSplash.alpha = 0f
            //unutma
            //binding.ConstraintLayoutSplash.animate().setDuration(2500).alpha(1f).withEndAction {
            binding.ConstraintLayoutSplash.animate().setDuration(500).alpha(1f).withEndAction {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }


    }
}