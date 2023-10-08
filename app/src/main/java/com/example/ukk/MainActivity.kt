package com.example.ukk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.ukk.TipeKamar.TipeActivity
import com.example.ukk.Transaksi.TransaksiActivity
import com.example.ukk.user.UserActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        //menerima intent info login
        val intent = intent


        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    // Tambahkan kode untuk menangani item "Home" disini
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }

                R.id.navigation_profile -> {
                    //kode user
                    startActivity(Intent(this, UserActivity::class.java))
                    true
                }

                R.id.navigation_kmr -> {
                    //kode kmr
                    startActivity(Intent(this, TipeActivity::class.java))
                    true
                }

                R.id.navigation_reser -> {
                    //reservasi
                    startActivity(Intent(this,TransaksiActivity::class.java))
                    true
                }


                else -> false
            }
        }
    }
}