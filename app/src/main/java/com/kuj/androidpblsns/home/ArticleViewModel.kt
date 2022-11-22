package com.kuj.androidpblsns.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.kuj.androidpblsns.home.ArticleModel
/**
 * 전체 데이터는 여기서 관리하면 됩니다.
 */
class ArticleViewModel : ViewModel() {
   // private val storage : FirebaseStorage by lazy{ FirebaseStorage.getReference}
   // private val auth: FirebaseAuth by lazy{}
   //private lateinit var database:DatabaseReference
   //private val myArticle = database.getReference("articles")

    private val database = Firebase.database
    private val myArticle = database.getReference("articles")

    private val articleDataList = mutableListOf<ArticleModel>()
    private val _articleLiveData = MutableLiveData<List<ArticleModel>>()
    val articleLiveData: LiveData<List<ArticleModel>> get() = _articleLiveData

    init {

        /* TODO Dummy 데이터 추가합니다.*/
        //addArticleModel(ArticleModel("asd", "ddd", 2000000L, "20000", ""))
        //addArticleModel(ArticleModel("asd", "ddd222", 2000000L, "20000", ""))

        /**
         * TODO 초기 앱이 켜질 때 Firebase Storage에서 데이터 가져올 것.
         */
        myArticle.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<ArticleModel>()
                Log.d(TAG, "Value is: $value")
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun addArticleModel(model: ArticleModel) {
        articleDataList.add(model)
        _articleLiveData.value = articleDataList
    }
}