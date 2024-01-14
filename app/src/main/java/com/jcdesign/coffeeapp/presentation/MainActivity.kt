package com.jcdesign.coffeeapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.jcdesign.coffeeapp.R
import com.jcdesign.coffeeapp.data.UserPreferences
import com.jcdesign.coffeeapp.presentation.ui.auth.AuthActivity
import com.jcdesign.coffeeapp.presentation.ui.location.LocationActivity
import com.jcdesign.coffeeapp.presentation.ui.startNewActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userPreferences = UserPreferences(this)

        userPreferences.authToken.asLiveData().observe(this, Observer {
            val activity = if (it == null) AuthActivity::class.java else LocationActivity::class.java
            startNewActivity(activity)
        })


    }
}