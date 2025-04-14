package com.example.diaryapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Устанавливаем ActionBar
        setSupportActionBar(findViewById(R.id.toolbar))

        // Добавим задержку для инициализации NavController
        try {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment

            val navController = navHostFragment?.navController
                ?: throw IllegalStateException("NavController not found")

            // Настроим ActionBar с NavController
            setupActionBarWithNavController(navController)
            Log.d("MainActivity", "NavController successfully initialized")

        } catch (e: Exception) {
            Log.e("MainActivity", "Error initializing NavController: ${e.message}")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment

        val navController = navHostFragment?.navController
            ?: return super.onSupportNavigateUp()

        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}