package com.kivous.infobytetechnologies.home.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kivous.infobytetechnologies.R
import com.kivous.infobytetechnologies.auth.ui.activities.AuthActivity
import com.kivous.infobytetechnologies.databinding.ActivityHomeBinding
import com.kivous.infobytetechnologies.utils.Common.auth

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.tvEmail.text = auth.currentUser?.email.toString()
        binding.btnLogOut.setOnClickListener {
            auth.signOut()
            startActivity(
                Intent(
                    this,
                    AuthActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
            finishAffinity()
        }

    }
}