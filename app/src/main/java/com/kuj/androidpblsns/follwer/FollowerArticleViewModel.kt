package com.kuj.androidpblsns.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.HashMap

/**
 * 전체 데이터는 여기서 관리하면 됩니다.
 */
class FollowerArticleViewModel : ViewModel() {

    private val database = Firebase.database
    private val myArticle = database.getReference("articles")
    private val firebaseAuth:FirebaseAuth by lazy{Firebase.auth}
    private val userRef =  Firebase.database.getReference("user")

    private val articleDataList = mutableListOf<ArticleModel>()
    private val _articleLiveData = MutableLiveData<List<ArticleModel>>()
    val articleLiveData: LiveData<List<ArticleModel>> get() = _articleLiveData

    init {

        myArticle.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (articleSnapShot in dataSnapshot.children) {
                    val article = articleSnapShot.getValue(ArticleModel::class.java)
                    if (article != null) {
                        isFollowerArticle(article)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun isFollowerArticle(model: ArticleModel){
        userRef.child(firebaseAuth.currentUser?.uid!!).child("following").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val following = dataSnapshot.value as HashMap<String, Boolean>
                val findUid = model.sellerId
                if(following.containsKey(findUid) && following.getValue(findUid) == true ){
                    addArticleModel(model)
                }
            }
        })
    }

    fun addArticleModel(model: ArticleModel) {
        articleDataList.add(model)
        _articleLiveData.value = articleDataList
    }

}