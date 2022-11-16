package com.kuj.androidpblsns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//메인 = 로그인 액티비티
class MainActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    var backKeyPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.v("Acitivity", "메인액티비티, 로그인")
        //auth = FirebaseAuth.getInstance()
        auth = Firebase.auth
        val signupButton = findViewById<Button>(R.id.signupButton)
        val loginButton = findViewById<Button>(R.id.loginButton)

        // 회원가입 버튼 -> SignUpActivity
        signupButton.setOnClickListener {
            Log.v("Click", "회원가입버튼 클릭")
            startActivity(Intent(this,SignUpActivity::class.java))
        }

        // 로그인 버튼
        loginButton.setOnClickListener {
            Log.v("Click", "로그인버튼 클릭")
            val idEditText = findViewById<EditText>(R.id.idEditText).text.toString()
            val passwordEditText = findViewById<EditText>(R.id.passwordEditText).text.toString()
            signIn(idEditText,passwordEditText)
        }
    }

   // 로그아웃하지 않을 시 자동 로그인 , 회원가입시 바로 로그인 됨
    public override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }

    // 로그인
    private fun signIn(email: String, password: String) {
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this, "로그인에 성공 하였습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    moveMainPage(auth?.currentUser)
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