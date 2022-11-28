package com.kuj.sangsangmarket

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.kuj.sangsangmarket.databinding.ActivityProfileEditBinding

class ProfileEditActivity : Activity() {

    private val binding by lazy { ActivityProfileEditBinding.inflate(layoutInflater) }
    private var firebaseAuth : FirebaseAuth? = null
    val database =  FirebaseDatabase.getInstance().reference
    val mypageRef = database.child("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        firebaseAuth = Firebase.auth

        // 유저별로 uid 가져옴
        val uid = firebaseAuth?.currentUser!!.uid

        // 버튼 누를시 이름 변경 후 마이페이지 fragment로
        binding.editNickNameBtn.setOnClickListener {
            // 바꿀이름
            val NametoChange = binding.editNickName.text.toString()
            //Log.v("check", NametoChange)

            mypageRef.child(uid).child("nickname").setValue(NametoChange)
                .addOnCompleteListener(this){
                        task -> if(task.isSuccessful){
                    Toast.makeText(this, "닉네임 변경 완료", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(
                        this, "변경에 실패 하였습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                }
        }
    }
}