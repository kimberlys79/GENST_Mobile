package com.example.genst.view.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.genst.R
import com.example.genst.databinding.ActivityMainBinding
import com.example.genst.view.ui.home.HomeViewModel
import com.example.genst.view.ui.startpage.startPage
import com.example.genst.view.utils.FactoryViewModel

class MainActivity : AppCompatActivity() {

    private val session by viewModels<HomeViewModel> {
        FactoryViewModel.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session.getSession().observe(this) { user ->
            Log.d("MainActivity", "User not logged in ${user.isLogin}")
            if (!user.isLogin) {
                startActivity(Intent(this, startPage::class.java))
                finish()
            } else {
                setupUI()
            }
        }
    }

    private fun setupUI() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)
    }
}