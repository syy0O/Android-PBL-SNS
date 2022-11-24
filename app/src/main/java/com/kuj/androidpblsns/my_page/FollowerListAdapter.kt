package com.kuj.androidpblsns.my_page

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kuj.androidpblsns.alarm.AlarmDTO
import com.kuj.androidpblsns.databinding.FollowlistBinding
import kotlinx.android.synthetic.main.followlist.view.*

class FollowerListAdapter(private val context: Context):
    RecyclerView.Adapter<FollowerListAdapter.ViewHolder>(){
    var datas = mutableListOf<FollowerData>()
    private var firebaseAuth : FirebaseAuth? = null
    val database =  FirebaseDatabase.getInstance().reference
    val mypageRef = database.child("user")


    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int): ViewHolder {
        val binding = FollowlistBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)

        // 버튼 클릭시
        // 팔로우 상태가 아니면 '팔로우'
        // 팔로우 상태면 '팔로우 취소'
        val followBtn = binding.followListBtn
        //if ~ 팔로우했는지 체크, 안 되어있다면 followBtn.text = "팔로우"
        //팔로우 되어있다면 followBtn.text = "팔로우취소", 디폴트 text가 팔로우 취소이긴 하다.

        followBtn.setOnClickListener {
            if(followBtn.text == "팔로우취소") { // DB에서 팔로우 상태이고 버튼을 눌렀을 때,
                followBtn.text = "팔로우" // Text Change,
                // 해당 유저 알림 Activity에 알림 띄우기

                // DB에서 팔로우 없애기
                Toast.makeText(context, "팔로우를 취소했습니다.", Toast.LENGTH_SHORT).show()
            } else{
                followBtn.text = "팔로우취소"
                // DB에 팔로우 저장
                Toast.makeText(context, "유저를 팔로우 합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size


    override fun onBindViewHolder(holder : ViewHolder, position: Int){
        holder.bind(datas[position])

    }

    inner class ViewHolder(private val binding : FollowlistBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(followerData: FollowerData){
            binding.followerEmail.text = followerData.email
            binding.followerNickname.text = followerData.nickname
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


