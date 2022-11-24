package com.kuj.androidpblsns.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.R
import com.kuj.androidpblsns.databinding.ActivitySignupBinding
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : AppCompatActivity(){

    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater) }
    private val firebaseAuth by lazy { Firebase.auth }
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //DB 초기화
        dbRef = Firebase.database.reference

        // 회원가입 버튼
        binding.signupOkButton.setOnClickListener {
            createAccount(binding.signupID.text.toString().trim(),binding.signupPassword.text.toString().trim(),
                binding.signupName.text.toString().trim())
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
    private fun createAccount(email: String, password: String, nickname: String) {

        val checkPassword = binding.CheckPassword.text.toString()

        if(!isValidEmail(email)){ // 이메일이 유효하지 않다
            Toast.makeText(this, "유효하지 않은 이메일 형식입니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (!isValidPasswd(password)){ // 비밀번호가 6자리 미만이다
            Toast.makeText(this, "비밀번호는 6자리 이상으로 만들어주세요", Toast.LENGTH_SHORT).show()
            return
        }

        if(signupPassword.text.toString() != checkPassword){
            Log.v("check", signupPassword.text.toString())
            Log.v("check", checkPassword)
            Toast.makeText(this, "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "계정 생성 완료.", Toast.LENGTH_SHORT).show()
                    addUserToDB(nickname, email, firebaseAuth.currentUser?.uid!!)
                    finish() // 가입창 종료
                } else {
                    Toast.makeText(this, "계정 생성 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDB(nickname : String, email:String, uid : String){
        var hashMap = HashMap<String,Boolean>()
        hashMap.put("default",false)
        dbRef.child("user").child(uid).setValue(UserData(nickname,email,uid,hashMap,0))
    }
}