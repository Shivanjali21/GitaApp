package com.practice.shreebhagavadgita.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.practice.shreebhagavadgita.R
import com.practice.shreebhagavadgita.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
      ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.activityRootMain)

        val navHost = supportFragmentManager.findFragmentById(R.id.navHostFCV) as NavHost
        navController = navHost.navController
    }
}