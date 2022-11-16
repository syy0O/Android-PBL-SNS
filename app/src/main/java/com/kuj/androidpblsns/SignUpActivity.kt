package com.kuj.androidpblsns

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity(){
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContentView(R.layout.activity_signup)

        val signup_okButton = findViewById<Button>(R.id.signup_okButton)
        val signupID = findViewById<EditText>(R.id.signupID)
        val signupPassword = findViewById<EditText>(R.id.signupPassword)
        val signupName = findViewById<EditText>(R.id.signupName)

        // 회원가입 버튼
        signup_okButton.setOnClickListener {
            createAccount(signupID.text.toString(),signupPassword.text.toString())
        }
    }

    //이메일 유효성 검사
    private fun isValidEmail(target: String): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    // 비밀번호 6자리 유효성 검사
    private fun isValidPasswd(target: String): Boolean {
        return target.length >= 6
    }

    // 회원가입 기능
    private fun createAccount(email: String, password: String) {

            if(!isValidEmail(email)){ // 이메일이 유효하지 않다
                Toast.makeText(
                    this, "유효하지 않은 이메일 형식입니다.",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            if (!isValidPasswd(password)){ // 비밀번호가 6자리 미만이다
                Toast.makeText(this, "비밀번호는 6자리 이상으로 만들어주세요",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            this, "계정 생성 완료.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // 가입창 종료
                    } else {
                        Toast.makeText(
                            this, "계정 생성 실패",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
    }
}