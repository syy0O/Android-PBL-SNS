package com.kuj.androidpblsns

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.home.ArticleModel

/**
 * 전체 데이터는 여기서 관리하면 됩니다.
 */
class ArticleViewModel : ViewModel() {

    private val database = Firebase.database
    private val myArticle = database.getReference("articles")
    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
    private val userRef = Firebase.database.getReference("user")

    private val myArticleDataList = mutableListOf<ArticleModel>()
    private val followerDataList = mutableListOf<ArticleModel>()
    private val articleDataList = mutableListOf<ArticleModel>()
    private val searchArticleDataList = mutableListOf<ArticleModel>()

    private val _articleLiveData = MutableLiveData<List<ArticleModel>>()
    val articleLiveData: LiveData<List<ArticleModel>> get() = _articleLiveData

    val searchSuccess = MutableLiveData(false)
    val editProfileSuccess = MutableLiveData<Boolean?>(null)

    val isLoading = MutableLiveData(true)

    init {

        /**
         * TODO 초기 앱이 켜질 때 Firebase Storage에서 데이터 가져올 것.
         */
        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val articleModel = snapshot.getValue(ArticleModel::class.java)
                articleModel ?: return

                addArticleModel(articleModel)
                updateFollowArticle()
                updateMyArticle()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        }
        myArticle.addChildEventListener(listener)
    }

    fun updateFollowArticle() {
        isLoading.value = true
        userRef.child(firebaseAuth.currentUser?.uid!!).child("following").get()
            .addOnSuccessListener {
                Log.d("song2", "여기 불림2")
                followerDataList.clear()

                val following = it.value as HashMap<String, Boolean>
                Log.d("song2", "${following}")

                for (articleModel in articleDataList) {
                    val findUid = articleModel.sellerId

                    if (following.containsKey(findUid) && following.getValue(findUid) == true) {
                        addFollowerArticleModel(articleModel)
                    } else {
                        //deleteArticleModel(model)
                    }
                }
                isLoading.value = false
            }
    }

    fun updateMyArticle() {
        for (articleModel in articleDataList) {
            if (articleModel.sellerId == firebaseAuth.currentUser?.uid) {
                addMyArticleModel(articleModel)
            } else {
                //deleteArticleModel(model)
            }
        }
    }

    fun initHomeFragmentData() {
        _articleLiveData.value = articleDataList
    }

    fun initFollowerFragmentData() {
        _articleLiveData.value = followerDataList
    }

    fun initMyArticleData() {
        _articleLiveData.value = myArticleDataList
    }

    fun addArticleModel(model: ArticleModel) {
        articleDataList.add(model)
        _articleLiveData.value = articleDataList
    }

    fun searchArticleModel(title: String) {
        searchArticleDataList.clear()
        for (articleModel in articleDataList) {
            if (articleModel.title.contains(title)) {
                searchArticleDataList.add(articleModel)
            }
        }
        _articleLiveData.value = searchArticleDataList
        searchSuccess.value = true
    }

    fun addArticleModelList(articleModeList: List<ArticleModel>) {
        for (articleModel in articleModeList) {
            articleDataList.add(articleModel)
        }
        _articleLiveData.value = articleDataList
    }

    fun addFollowerArticleModel(model: ArticleModel) {
        followerDataList.add(model)
    }

    fun deleteFollowerArticleModel(model: ArticleModel) {
        followerDataList.remove(model)
    }

    fun addMyArticleModel(model: ArticleModel) {
        myArticleDataList.add(model)
    }

    fun removeMyArticleModel(model: ArticleModel) {
        myArticleDataList.remove(model)
    }

    fun editUserNickName(nickname: String) {
        firebaseAuth.currentUser?.let {
            userRef.child(it.uid).child("nickname").setValue(nickname).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    editProfileSuccess.value = true
                } else {
                    editProfileSuccess.value = false
                }
            }
        }
    }
}