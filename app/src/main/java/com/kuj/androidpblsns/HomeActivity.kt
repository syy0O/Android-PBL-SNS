package com.kuj.androidpblsns

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.home.ArticleModel

// 홈 액티비티
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bnv_home = findViewById<BottomNavigationView>(R.id.bnv)

        bnv_home.setOnItemSelectedListener { item ->
            changeFragment(
                when (item.itemId) {
                    R.id.home -> {
                        HomeFragment()
                    }
                    R.id.follower -> {
                        FollowerFragment()
                    }
                    R.id.chatList -> {
                        ChatFragment()
                    }
                    else -> {
                        MyPageFragment()
                    }
                }
            )
            true
        }
        bnv_home.selectedItemId = R.id.home
    }
    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.framelayout, fragment)
            .commit()
    }
}