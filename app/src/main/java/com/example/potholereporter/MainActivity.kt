package com.example.potholereporter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.potholereporter.databinding.ActivityMainBinding
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {
    lateinit var mFirebaseAnalytics: FirebaseAnalytics
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val navController = this.findNavController(R.id.nav_host)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host)
        return navController.navigateUp()
    }

}
