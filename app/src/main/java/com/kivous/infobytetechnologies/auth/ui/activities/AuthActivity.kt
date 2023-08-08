package com.kivous.infobytetechnologies.auth.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.kivous.infobytetechnologies.R
import com.kivous.infobytetechnologies.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)

        binding.ivBackArrow.setOnClickListener { finish() }
        binding.ivSetting.setOnClickListener {}
        binding.lavWave.playAnimation()
    }
}