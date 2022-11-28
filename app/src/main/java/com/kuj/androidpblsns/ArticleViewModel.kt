package com.kuj.androidpblsns

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kuj.androidpblsns.data.FollowerData
import com.kuj.androidpblsns.data.UserData
import com.kuj.androidpblsns.home.ArticleModel

/**
 * 전체 데이터는 여기서 관리하면 됩니다.
 */
class ArticleViewModel : ViewModel() {

    private val database = Firebase.database
    private val myArticle = database.getReference("articles")
    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
    private val userRef = Firebase.database.getReference("user")

    private var followingUidList = mutableListOf<String>()
    private var followingTotalDataList = mutableListOf<FollowerData>()
    private val myArticleDataList = mutableListOf<ArticleModel>()
    private val followerDataList = mutableListOf<ArticleModel>()
    private val articleDataList = mutableListOf<ArticleModel>()
    private val searchArticleDataList = mutableListOf<ArticleModel>()

    private val _articleLiveData = MutableLiveData<List<ArticleModel>>()
    val articleLiveData: LiveData<List<ArticleModel>> get() = _articleLiveData

    private val _followingUidLiveData = MutableLiveData<List<String>>()
    val followingUidLiveData: LiveData<List<String>> get() = _followingUidLiveData

    private val _followingTotalDataLiveData = MutableLiveData<List<FollowerData>>()
    val followingTotalDataLiveData: LiveData<List<FollowerData>> get() = _followingTotalDataLiveData

    val searchSuccess = MutableLiveData(false)
    val editProfileSuccess = MutableLiveData<Boolean?>(null)

    val isLoading = MutableLiveData(true)

    init {
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

        userRef.child(firebaseAuth.currentUser?.uid!!).child("following").get()
            .addOnSuccessListener {
                val followingList = it.value as List<String>

                for (following in followingList) {
                    if (following == "don't touch this key") continue

                    userRef.child(following).get().addOnSuccessListener { data ->
                        val followerData = data.getValue(UserData::class.java)
                        followingTotalDataList.add(FollowerData(followerData?.nickname ?: "", followerData?.email ?: "", followerData?.uid ?: ""))

                        _followingTotalDataLiveData.value = followingTotalDataList
                    }
                }

                _followingUidLiveData.value = followingList
            }
    }

    fun updateFollowData(followList: MutableList<String>) {
        this.followingUidList = followList
        Log.d("song2", "updateFollowData: ${followList}")
        _followingUidLiveData.value = followingUidList
    }

    fun addSpecificFollower(uid: String) {
        followingUidList.add(uid)

        userRef.child(uid).get().addOnSuccessListener { data ->
            val followerData = data.getValue(UserData::class.java)
            followingTotalDataList.add(FollowerData(followerData?.nickname ?: "", followerData?.email ?: "", followerData?.uid ?: ""))

            _followingTotalDataLiveData.value = followingTotalDataList
        }

        _followingUidLiveData.value = followingUidList
    }

    fun removeSpecificFollower(uid: String) {
        followingUidList.remove(uid)
        for (followingTotalData in followingTotalDataList) {
            if (followingTotalData.uid == uid) {
                followingTotalDataList.remove(followingTotalData)
                break
            }
        }

        _followingUidLiveData.value = followingUidList
        _followingTotalDataLiveData.value = followingTotalDataList
    }

    fun updateFollowArticle() {
        isLoading.value = true
        userRef.child(firebaseAuth.currentUser?.uid!!).child("following").get()
            .addOnSuccessListener {
                Log.d("song2", "여기 불림2")
                followerDataList.clear()

                val following = it.value as List<String>
                Log.d("song2", "following ${following}")

                for (articleModel in articleDataList) {
                    val findUid = articleModel.sellerId

                    if (following.contains(findUid)) {
                        addFollowerArticleModel(articleModel)
                    } else {
                        //deleteArticleModel(model)
                    }
                }
                isLoading.value = false
            }
    }

    fun updateMyArticle() {
        Log.d("yousong","몇번")
        for (articleModel in articleDataList) {
            if (articleModel.sellerId == firebaseAuth.currentUser?.uid && !myArticleDataList.contains(articleModel)) {
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
        Log.d("eunseo","여기가 불려요오오오옹오ㅓ ")
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
                editProfileSuccess.value = task.isSuccessful
            }
        }
    }
}