package com.kuj.androidpblsns

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.kuj.androidpblsns.chat.ChatFragment

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.home.ArticleModel
import com.kuj.androidpblsns.databinding.ActivityHomeBinding
import com.kuj.androidpblsns.follwer.FollowerFragment

import com.kuj.androidpblsns.my_page.MyPageFragment
import com.kuj.androidpblsns.home.ArticleViewModel

// 홈 액티비티
class HomeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
   // private lateinit var database: DatabaseReference
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    //private val userRef =  Firebase.database.getReference("Articles")
    /** 이 객체가 초기화될 때 [ArticleViewModel]에서 init 발생 */
    private val viewModel by viewModels<ArticleViewModel>()

    private val articleDB: DatabaseReference by lazy {
        Firebase.database.reference.child("articles")
    }

    private val listener = object: ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val articleModel = snapshot.getValue(ArticleModel::class.java)
            articleModel ?: return

            viewModel.addArticleModel(articleModel)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }
        override fun onCancelled(error: DatabaseError) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        articleDB.addChildEventListener(listener)

        initBtmNavi()
    }

    private fun initBtmNavi() {
        binding.bnv.setOnItemSelectedListener { item ->
            changeFragment(
                when (item.itemId) {
                    R.id.home -> HomeFragment()
                    R.id.follower -> FollowerFragment()
                    R.id.chatList -> ChatFragment()
                    else -> MyPageFragment()
                }
            )
            true
        }
        binding.bnv.selectedItemId = R.id.home
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.framelayout, fragment)
            .commit()
    }


}