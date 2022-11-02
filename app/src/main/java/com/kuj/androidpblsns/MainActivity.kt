package com.kuj.androidpblsns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val fl : FrameLayout by lazy{
        findViewById((R.id.framelayout))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Firebase.auth.signInWithEmailAndPassword("admin@hsu.com", "hansung")
            .addOnCompleteListener{
                if (it.isSuccessful){
                    println("########### Login Success")
                }   else{
                    println("########### Login Failed")
                }
            }


        val bnv_home = findViewById<BottomNavigationView>(R.id.bnv)

        bnv_home.setOnItemSelectedListener { item ->
            changeFragment(
                when (item.itemId) {
                    R.id.home -> {
                        HomeFragment()
                        // Respond to navigation item 1 click
                    }
                    R.id.follower -> {
                        FollowerFragment()
                        // Respond to navigation item 2 click
                    }
                    R.id.chatList -> {
                        ChatFragment()
                        // Respond to navigation item 3 click
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