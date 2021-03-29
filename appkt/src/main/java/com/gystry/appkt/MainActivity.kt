package com.gystry.appkt

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gystry.appkt.utils.NavGraphBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        Log.e("TAG", "onCreate: $navView---$navController")
        NavigationUI.setupWithNavController(navView, navController)

        NavGraphBuilder.build(navController,this, fragment!!.id)

        nav_view.setOnNavigationItemSelectedListener {

            navController.navigate(it.itemId)
            return@setOnNavigationItemSelectedListener TextUtils.isEmpty(it.title)
        }
    }
}
