package com.jcdesign.coffeeapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.jcdesign.coffeeapp.R
import com.jcdesign.coffeeapp.data.UserPreferences
import com.jcdesign.coffeeapp.presentation.ui.auth.AuthActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userPreferences = UserPreferences(this)

        userPreferences.authToken.asLiveData().observe(this, Observer {
            Toast.makeText(this, it ?: "Token is null", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, AuthActivity::class.java))
        })


    }
}