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

import kotlin.collections.HashMap


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
                Log.d("song1","여기 불림1")

               // articleDataList.clear()

                for (articleSnapShot in dataSnapshot.children) {
                    val article = articleSnapShot.getValue(ArticleModel::class.java)
                    if (article != null) {
                        isFollowerArticle(article)
                    }
                }
               // _articleLiveData.value = articleDataList
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun isFollowerArticle(model: ArticleModel){
        userRef.child(firebaseAuth.currentUser?.uid!!).child("following").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("song2","여기 불림2")
                val following = dataSnapshot.value as HashMap<String, Boolean>
                val findUid = model.sellerId
                if(following.containsKey(findUid) && following.getValue(findUid) == true ){
                    addArticleModel(model)
                }
                else {
                    //deleteArticleModel(model)
                }
            }
        })
    }

    fun addArticleModel(model: ArticleModel) {
        articleDataList.add(model)
        _articleLiveData.value = articleDataList
    }

    fun deleteArticleModel(model: ArticleModel){
        articleDataList.remove(model)
       _articleLiveData.value = articleDataList
    }

}