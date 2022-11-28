package com.kuj.androidpblsns.my_page

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.ArticleViewModel
import com.kuj.androidpblsns.alarm.AlarmDTO
import com.kuj.androidpblsns.data.FollowerData
import com.kuj.androidpblsns.databinding.FollowlistBinding

class FollowerListAdapter(private val viewModel: ArticleViewModel, private val context: Context) :
    RecyclerView.Adapter<FollowerListAdapter.ViewHolder>() {

    private lateinit var database: DatabaseReference
    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
    private val userRef = Firebase.database.getReference("user")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FollowlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = viewModel.followingTotalDataLiveData.value?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel.followingTotalDataLiveData.value!![position])
    }

    inner class ViewHolder(private val binding: FollowlistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(followerData: FollowerData) {
            binding.apply {
                followerEmail.text = followerData.email
                followerNickname.text = followerData.nickname
                followListBtn.text = "UnFollow"

                followListBtn.setOnClickListener {
                    followingProcess(firebaseAuth.currentUser!!.uid, followerData.uid)
                    Toast.makeText(context, "팔로우를 취소했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun followingProcess(userId: String, followingId: String) {

        var myFollowingList: List<String> = emptyList()
        var followUserList: List<String> = emptyList()

        userRef.child(userId).child("following").get().addOnSuccessListener {
            myFollowingList = it.value as List<String>
            if (followListValidation(myFollowingList, followUserList)) {
                Log.d("파송송", "followValidation Success ${myFollowingList} ${followUserList}")
                unFollowProcess(userId,
                    followingId,
                    myFollowingList.toMutableList(),
                    followUserList.toMutableList())
            }
        }

        userRef.child(followingId).child("follower").get().addOnSuccessListener {
            followUserList = it.value as List<String>
            if (followListValidation(myFollowingList, followUserList)) {
                Log.d("파송송", "followValidation Success ${myFollowingList} ${followUserList}")
                unFollowProcess(userId,
                    followingId,
                    myFollowingList.toMutableList(),
                    followUserList.toMutableList())
            }
        }
    }

    private fun followListValidation(myList: List<String>, followList: List<String>): Boolean {
        if (myList.isNotEmpty() && followList.isNotEmpty()) {
            return true
        }
        return false
    }

    private fun unFollowProcess(
        userId: String,
        followingId: String,
        myList: MutableList<String>,
        followList: MutableList<String>,
    ) {
        myList.remove(followingId)
        userRef.child(userId).child("following").setValue(myList)
        followList.remove(userId)
        userRef.child(followingId).child("follower").setValue(followList.toList())
        viewModel.removeSpecificFollower(followingId)
        notifyDataSetChanged()

        viewModel.updateFollowArticle()
    }

    fun followAlarm(destinationUid: String) {
        val alarmDTO = AlarmDTO()
        val uid = firebaseAuth.currentUser!!.uid
        alarmDTO.destinationUid = destinationUid
        alarmDTO.userId = database.child("user").child(uid).child("nickname").toString()
        alarmDTO.kind = 0
        alarmDTO.timestamp = System.currentTimeMillis()

    }

}


