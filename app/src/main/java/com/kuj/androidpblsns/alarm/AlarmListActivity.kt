package com.kuj.androidpblsns.alarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kuj.androidpblsns.databinding.ActivityAlarmlistBinding
import com.kuj.androidpblsns.my_page.AlarmListAdapter

class AlarmListActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAlarmlistBinding.inflate(layoutInflater) }
    private var firebaseAuth : FirebaseAuth? = null
    val database =  FirebaseDatabase.getInstance().reference
    val mypageRef = database.child("user")

    lateinit var alarmListAdapter: AlarmListAdapter
    val datas = mutableListOf<AlarmData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecycler()
    }
    private fun initRecycler(){
        alarmListAdapter = AlarmListAdapter(this)
        binding.recycleralarmlist.adapter = alarmListAdapter

        datas.apply{
            add(AlarmData(alarm = "dd 님이 팔로우하셨습니다"))

        }
        alarmListAdapter.datas = datas
        alarmListAdapter.notifyDataSetChanged()

    }
}