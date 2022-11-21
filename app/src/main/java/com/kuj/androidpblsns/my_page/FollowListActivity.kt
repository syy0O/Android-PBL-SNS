package com.kuj.androidpblsns.my_page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kuj.androidpblsns.databinding.ActivityFollowlistBinding

class FollowListActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFollowlistBinding.inflate(layoutInflater) }
    private var firebaseAuth : FirebaseAuth? = null
    val database =  FirebaseDatabase.getInstance().reference
    val mypageRef = database.child("user")

    lateinit var followerListAdapter: FollowerListAdapter
    val datas = mutableListOf<FollowerData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecycler()
    }
    private fun initRecycler(){
        followerListAdapter = FollowerListAdapter(this)
        binding.recyclerfollowerlist.adapter = followerListAdapter

        datas.apply{
            add(FollowerData(nickname = "김은서", email = "kes@naver.com"))
            add(FollowerData(nickname = "유송연", email = "ysy@naver.com"))
            add(FollowerData(nickname = "정예윤", email = "jyy@naver.com"))
        }
        followerListAdapter.datas = datas
        followerListAdapter.notifyDataSetChanged()

    }
}