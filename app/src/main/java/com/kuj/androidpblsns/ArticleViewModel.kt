package com.kuj.androidpblsns


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuj.androidpblsns.home.ArticleModel


/**
 * 전체 데이터는 여기서 관리하면 됩니다.
 */
class ArticleViewModel : ViewModel() {

    private val articleDataList = mutableListOf<ArticleModel>()
    private val _articleLiveData = MutableLiveData<List<ArticleModel>>()
    val articleLiveData: LiveData<List<ArticleModel>> get() = _articleLiveData

    init {

        /* TODO Dummy 데이터 추가합니다.*/
//        addArticleModel(ArticleModel("asd", "ddd", 2000000L, "20000", ""))
//        addArticleModel(ArticleModel("asd", "ddd222", 2000000L, "20000", ""))

        /**
         * TODO 초기 앱이 켜질 때 Firebase Storage에서 데이터 가져올 것.
         */
    }

    fun addArticleModel(model: ArticleModel) {
        articleDataList.add(model)
        _articleLiveData.value = articleDataList
    }
}