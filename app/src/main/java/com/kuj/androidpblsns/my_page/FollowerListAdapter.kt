package com.kuj.androidpblsns.my_page

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ListAdapter
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.alarm.AlarmDTO
import com.kuj.androidpblsns.databinding.FollowlistBinding
import com.kuj.androidpblsns.push.FcmPush
import kotlinx.android.synthetic.main.followlist.view.*
import java.util.HashMap

class FollowerListAdapter(private val viewModel: FollowingListViewModel,private val context: Context):
    RecyclerView.Adapter<FollowerListAdapter.ViewHolder>(){
    var datas = mutableListOf<FollowerData>()
    private val firebaseAuth:FirebaseAuth by lazy{ Firebase.auth}
    private val userRef =  Firebase.database.getReference("user")
    val database =  FirebaseDatabase.getInstance().reference
    val mypageRef = database.child("user")

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int): ViewHolder {
        val binding = FollowlistBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = viewModel.followingListLiveData.value?.size ?: 0


    override fun onBindViewHolder(holder : ViewHolder, position: Int){
        holder.bind(viewModel.followingListLiveData.value!![position])
    }

    inner class ViewHolder(private val binding : FollowlistBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(followerData: FollowerData){
            binding.followerEmail.text = followerData.email
            binding.followerNickname.text = followerData.nickname
            binding.followListBtn.setOnClickListener{
                if(binding.followListBtn.text == "팔로우 취소") { // DB에서 팔로우 상태이고 버튼을 눌렀을 때,
                    binding.followListBtn.text = "팔로우" // Text Change,
                    // 해당 유저 알림 Activity에 알림 띄우기
                    // DB에서 팔로우 없애기
                    userRef.child(firebaseAuth.currentUser?.uid!!).child("following").child(followerData.uid).setValue(false)
                    Toast.makeText(context, "팔로우를 취소했습니다.", Toast.LENGTH_SHORT).show()
                } else{
                    binding.followListBtn.text = "팔로우 취소"
                    // DB에 팔로우 저장
                    userRef.child(firebaseAuth.currentUser?.uid!!).child("following").child(followerData.uid).setValue(true)
                    Toast.makeText(context, "유저를 팔로우 합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun followAlarm(destinationUid : String){
        var alarmDTO = AlarmDTO()
        val uid = firebaseAuth?.currentUser!!.uid
        alarmDTO.destinationUid = destinationUid
        alarmDTO.userId = database.child("user").child(uid).child("nickname").toString()
        alarmDTO.kind=0
        alarmDTO.timestamp = System.currentTimeMillis()

    }

}


