package com.kuj.androidpblsns.my_page


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.chat.ChatData
import com.kuj.androidpblsns.data.Message
import java.util.HashMap

class FollowingListViewModel : ViewModel(){

    private val database = Firebase.database
    private val userRef =  Firebase.database.getReference("user")
    private val auth: FirebaseAuth by lazy{ Firebase.auth}

    //접속자 Uid
    private val userUid = auth.currentUser?.uid


    // following list
    private val followingList = mutableListOf<FollowerData>()
    private val _followingListLiveData = MutableLiveData<List<FollowerData>>()
    val followingListLiveData get() = _followingListLiveData

    val setFollowingListFromDBSuccess = MutableLiveData(false)
    val removeFollowing = MutableLiveData(false)


    // DB 객체 이용해 메시지 가져오기
    fun getFollowingListFromDB() {
        userRef.child(userUid!!).child("following")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //데이터 가져오는 기능 구현
                    followingList.clear()
                    val following = snapshot.value as HashMap<String, Boolean>
                    val followingUids = following.filterValues { it == true }.keys
                    for (following in followingUids){
                        addFollowingList(following)
                    }
                    _followingListLiveData.value = followingList
                    setFollowingListFromDBSuccess.value = true
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun addFollowingList(uid:String){
        userRef.child(uid).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //데이터 가져오는 기능 구현
                val map = snapshot.value as Map<*, *>
                var nickname = map["nickname"].toString()
                var email = map["email"].toString()
                val followingInfo = FollowerData(nickname,email)
                followingList.add(followingInfo)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}