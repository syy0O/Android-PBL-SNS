package com.kuj.androidpblsns.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.HomeActivity
import com.kuj.androidpblsns.R
import com.kuj.androidpblsns.databinding.ActivityMainBinding

//메인 = 로그인 액티비티
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val firebaseAuth by lazy { Firebase.auth }
    var backKeyPressedTime: Long = 0
    //private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.v("Acitivity", "메인액티비티, 로그인")
        //auth = FirebaseAuth.getInstance()

        // 회원가입 버튼 -> SignUpActivity
        binding.signupButton.setOnClickListener {
            Log.v("Click", "회원가입버튼 클릭")
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // 로그인 버튼
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

    // 홈 액티비티 호출
    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
    // 뒤로가기 2번 exit
    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2500){
            Toast.makeText(
                this, "뒤로가기 버튼을 한번 더 누르면 종료합니다.",
                Toast.LENGTH_SHORT
            ).show()
            backKeyPressedTime = System.currentTimeMillis()
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500){
            finishAffinity()
        }
    }
}