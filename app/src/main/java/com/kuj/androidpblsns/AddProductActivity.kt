package com.kuj.androidpblsns


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout.TabGravity
import com.google.firebase.storage.FirebaseStorage
import com.kuj.androidpblsns.databinding.AddProductBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class AddProductActivity : AppCompatActivity() {

    var PICK_IMAGE_FROM_ALBUM = 2000
    var storage : FirebaseStorage? = null
    var photoUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.add_product)

        // initiate storage
        storage = FirebaseStorage.getInstance()

        // 사진 선택 버튼 액션
        findViewById<Button>(R.id.camera_button).setOnClickListener{
            when {
                ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                    // READ_EXTERNAL_STORAGE의 권한이 PERMISSION_GRANTED와 같다면..
                    //TODO 권한이 잘 부여되었을 때상황, 갤러리에서 사진을 선택하는 코드 구현
                    navigatePhotos()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    // 권한을 명시적으로 거부한 경우 true
                    // 처음보거나, 다시묻지 않음을 선택한 경우 false
                    //TODO 교육용 팝업 확인 후 권한 팝업을 띄우는 기능
                    showPermissionContextPopup()
                }
                else -> {
                    // 처음봤을때 띄워줌
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
                }
            }
        }

        findViewById<Button>(R.id.cancle_button).setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java);
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            //overridePendingTransition(R.anim.none,R.anim.slide_up_exit)
        }

        findViewById<Button>(R.id.upload_button).setOnClickListener{
            contentUpload()
        }


    } // end of func OnCreate..

    private fun showPermissionContextPopup() { // 권한 허용 팝업
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다")
            .setMessage("전자액자에서 사진을 선택하려면 권한이 필요합니다.")
            .setPositiveButton("동의하기", {_, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
            })
            .setNegativeButton("취소하기",{ _,_ ->})
            .create()
            .show()
    } // end of func showPermissonContextPopup..

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1000 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // 권한이 부여 된 것입니다.
                    // 허용 클릭 시
                    navigatePhotos()
                } else {
                    // 거부 클릭시
                    Toast.makeText(this,"권한을 거부했습니다.",Toast.LENGTH_SHORT).show()
                }
            } else -> {
                //Do Nothing
            }
        }
    }// end of func onRequestPermissionsResult..

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // 선택한 이미지를 받는 부분
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_FROM_ALBUM){
            if(resultCode == Activity.RESULT_OK){ // 사진을 선택 했을 때 -> 이미지의 경로가 넘어옴
                photoUri = data?.data // 경로(path)
                findViewById<ImageView>(R.id.addphoto_image).setImageURI(photoUri) // 이미지 뷰에 선택한 이미지 표시
            }
            else{ // 취소버튼을 눌렀을 때 작동하는 부분
                finish()
            }
        }
    } // end of func onActivityResult..

    private fun navigatePhotos() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,2000)
    }//end of func navigatePhotos..

    private fun contentUpload(){

        Log.d("contentUpload func 들어옴: ", " ")
        //Make filename
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_"+timestamp+"_.png"

        var storageRef = storage?.reference?.child("images")?.child(imageFileName)
        Log.d("firebase storage ref: ", " "+storageRef)
        Log.d("firebase photoUri: ", " "+photoUri!!)

        //FileUpload
        storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            Toast.makeText(this, getString(R.string.upload_success), Toast.LENGTH_LONG).show()
        }

    }
}