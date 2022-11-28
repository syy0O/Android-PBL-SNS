package com.kuj.androidpblsns

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*

import com.kuj.androidpblsns.chat.ChatFragment

import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.kuj.androidpblsns.home.ArticleModel
import com.kuj.androidpblsns.databinding.ActivityHomeBinding
import com.kuj.androidpblsns.follwer.FollowerFragment

import com.kuj.androidpblsns.my_page.MyPageFragment
import com.kuj.androidpblsns.home.HomeFragment
import com.kuj.androidpblsns.search.SearchListFragment


// 홈 액티비티
class HomeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    /** 이 객체가 초기화될 때 [ArticleViewModel]에서 init 발생 */
    private val viewModel by viewModels<ArticleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //getFCMToken()
        registerPushToken()
        initBtmNavi()

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    // 앱 실행시 토큰 할당. 팔로우 할 경우 Notification을 전송하기 위함
    /*private fun getFCMToken() : String? {
        var token : String? = null
        val uid = firebaseAuth.currentUser?.uid!!
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
            task-> if (!task.isSuccessful){
              Log.w(TAG, "Fetching FCM registration token failed", task.exception)
             return@OnCompleteListener
        }
            token = task.result
            Log.d(TAG, "FCM Token is ${token}")
            dbRef.child("user").child(uid).child("token").setValue(token)

        })
        return token
    }*/

    private fun registerPushToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            val token = task.result
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val map = mutableMapOf<String, Any>()
            map["pushToken"] = token!!

            FirebaseFirestore.getInstance().collection("pushtokens").document(uid!!).set(map)
        }
    }

    private fun initBtmNavi() {
        binding.bnv.setOnItemSelectedListener { item ->
            changeFragment(
                when (item.itemId) {
                    R.id.home -> HomeFragment()
                    R.id.follower -> FollowerFragment()
                    R.id.searchList -> SearchListFragment()
                    else -> MyPageFragment()
                }
            )
            true
        }
        binding.bnv.selectedItemId = R.id.home
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.framelayout, fragment)
            .commit()
    }

    fun changeFragmentWithBackStack(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.framelayout, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun removeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .remove(fragment)
            .commit()
    }
}