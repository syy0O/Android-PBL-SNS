package com.kuj.androidpblsns.alarm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AlarmRepo {
    fun getData() : LiveData<MutableList<AlarmData>>{
        val mutableData = MutableLiveData<MutableList<AlarmData>>()
        val firebaseAuth = Firebase.auth
        val database =  FirebaseDatabase.getInstance().reference
        val AlarmRef = database.child("user").child(firebaseAuth.currentUser!!.uid).child("follower")
        Log.v("test", AlarmRef.toString())
        AlarmRef.addValueEventListener(object : ValueEventListener {
            val listData: MutableList<AlarmData> = mutableListOf()
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (alarmSnapshot in snapshot.children){
                        val getData = alarmSnapshot.getValue(AlarmData ::class.java)
                        Log.v("###@# ", getData.toString())
                        listData.add(getData!!)
                        mutableData.value = listData
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return mutableData
    }
}