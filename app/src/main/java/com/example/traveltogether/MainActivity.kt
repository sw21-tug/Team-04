package com.example.traveltogether

import com.firebase.ui.auth.AuthUI
import org.jetbrains.anko.intentFor
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.Tasks
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var navControllerSlide: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.buttom_menu_fragment)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.saved_post_fragment, R.id.all_post_fragment, R.id.chat_fragment, R.id.new_popup_fragment), drawerLayout)
        bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController,appBarConfiguration)

        val navigationView = findViewById<View>(R.id.slideNavigationView) as NavigationView
        navigationView.setNavigationItemSelectedListener { menuItem ->
            val id: Int = menuItem.getItemId()
            if (id == R.id.profile_fragment) {
                startActivity(intentFor<ProfileActivity>().newTask().clearTask())
            } else if (id == R.id.logout_fragment) {
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener { // user is now signed out
                            startActivity(intentFor<SignIn>().newTask().clearTask())
                            finish()
                        }
            } else if (id == R.id.settings_fragment) {
                startActivity(intentFor<SettingActivity>().newTask().clearTask())
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        // val navController = findNavController(R.id.slide_menu_fragment)
        val navController = findNavController(R.id.buttom_menu_fragment)
        return navController.navigateUp(appBarConfiguration)
                ||  super.onSupportNavigateUp()
    }
}