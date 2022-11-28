package com.kuj.sangsangmarket

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.kuj.sangsangmarket.databinding.ActivityMyPageBinding

class MyPageActivity : Activity() {

    private lateinit var binding: ActivityMyPageBinding
    private var firebaseAuth : FirebaseAuth? = null
    val database =  FirebaseDatabase.getInstance().reference
    val mypageRef = database.child("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyPageBinding.inflate(layoutInflater)
        firebaseAuth = Firebase.auth
        setContentView(binding.root)


        // 유저별로 uid 가져옴
        val uid = firebaseAuth?.currentUser!!.uid
        // DB에 저장되어있는 유저의 닉네임 가져옴
        val nickname = mypageRef.child(uid).child("nickname")
        // DB에 저장되어있는 유저의 이메일 가져옴
        val email = mypageRef.child(uid).child("email")

        //Log.v("test", "########" + mypageRef.child(uid).toString())

        // DB에 가져온 닉네임으로 editText 바꿔줌
        nickname.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val text = dataSnapshot.getValue(String::class.java)
                binding.userID.text = text
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        // DB에 가져온 이메일로 editText 바꿔줌
        email.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val text = dataSnapshot.getValue(String::class.java)
                binding.userEmail.text = text
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        // 유저 프로필 수정버튼
        binding.profileEdit.setOnClickListener {
            startActivity(Intent(this, ProfileEditActivity::class.java))
        }

        // 로그아웃 후 로그인 화면으로
        binding.logoutBtn.setOnClickListener {

            firebaseAuth?.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
