package com.example.traveltogether

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navControllerSlide: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var listener: NavController.OnDestinationChangedListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.buttom_menu_fragment)

        val slideNavigationView = findViewById<NavigationView>(R.id.slide_menu_fragment)
        navControllerSlide = findNavController(R.id.slide_menu_fragment)
        drawerLayout = findViewById(R.id.drawer_layout)
        bottomNavigationView.setupWithNavController(navController)
        slideNavigationView.setupWithNavController(navControllerSlide)
        appBarConfiguration = AppBarConfiguration(navControllerSlide.graph, drawerLayout)
        setupActionBarWithNavController(navControllerSlide,appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.slide_menu_fragment)
        return navController.navigateUp(appBarConfiguration)
         ||  super.onSupportNavigateUp()
    }
}