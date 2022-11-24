package com.kuj.androidpblsns.my_page

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.R
import com.kuj.androidpblsns.alarm.AlarmDTO
import com.kuj.androidpblsns.databinding.FragmentMyPageBinding
import com.kuj.androidpblsns.login.MainActivity


class MyPageFragment : Fragment() {

    private lateinit var binding: FragmentMyPageBinding
    private var firebaseAuth : FirebaseAuth? = null
    val database =  FirebaseDatabase.getInstance().reference
    val mypageRef = database.child("user")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentMyPageBinding.inflate(inflater, container, false)
        firebaseAuth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        binding.profileEditBtn.setOnClickListener {
            val intent = Intent(context, ProfileEditActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)
        }

        // 팔로우 목록 보기
        binding.followCheckBtn.setOnClickListener {
            val intent = Intent(context, FollowListActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)
        }

        // 내가 쓴 글 모아보기
        binding.myProductBtn.setOnClickListener {
            val intent = Intent(context, MyProductActivity::class.java).apply{
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)
        }

        // 로그아웃 후 로그인 화면으로
        binding.logoutBtn.setOnClickListener {

            firebaseAuth?.signOut()
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            val intent = Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)
        }
    }

}