package com.kuj.sangsangmarket

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kuj.sangsangmarket.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    private val firebaseAuth by lazy { Firebase.auth }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loginButton.setOnClickListener {
            Log.v("Click", "로그인버튼 클릭")
            val idEditText = findViewById<EditText>(R.id.idEditText).text.toString()
            val passwordEditText = findViewById<EditText>(R.id.passwordEditText).text.toString()
            signIn(idEditText,passwordEditText)
        }
    }

    // 로그아웃하지 않을 시 자동 로그인 , 회원가입시 바로 로그인 됨
    public override fun onStart() {
        super.onStart()
        moveMainPage(firebaseAuth.currentUser)
    }
    // 로그인
    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this, "로그인에 성공 하였습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    moveMainPage(firebaseAuth.currentUser)
                } else {
                    Toast.makeText(
                        this, "로그인에 실패 하였습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
    // 마이페이지 액티비티 호출
    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MyPageActivity::class.java))
            finish()
        }
    }
}