package com.kuj.androidpblsns.my_page

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kuj.androidpblsns.data.FollowerData
import com.kuj.androidpblsns.databinding.ActivityFollowlistBinding

class FollowListActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFollowlistBinding.inflate(layoutInflater) }
    private var firebaseAuth : FirebaseAuth? = null
    val database =  FirebaseDatabase.getInstance().reference
    val mypageRef = database.child("user")

    lateinit var followerListAdapter: FollowerListAdapter
    val datas = mutableListOf<FollowerData>()
    private val viewModel by viewModels<FollowingListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initRecycler()
    }
    private fun initRecycler(){

        followerListAdapter = FollowerListAdapter(viewModel,this)
        binding.recyclerfollowerlist.adapter = followerListAdapter

        viewModel.setFollowingListFromDBSuccess.observe(this){ isSuccess->
           if(isSuccess) {
               followerListAdapter.notifyDataSetChanged()
               viewModel.setFollowingListFromDBSuccess.value = false
           }
        }
    }
}