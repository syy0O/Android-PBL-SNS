package com.kuj.androidpblsns.home

data class ArticleModel(
    var sellerId: String = "", //uid
    var title: String = "", //글 제목
    var createAt: Long = 0L, //timestamp
    var price: String = "", //가격
    var content:String="", //글 내용
    var imageUrl: String = "", //이미지 url
    var favoriteCount:Int = 0, // 찜 개수,
    var favorites:Map<String,Boolean> = HashMap() // 중복 좋아요(찜) 방지
)

//data class ArticleModel(
//    val sellerId:String,
//    val title: String,
//    val createAt: Long,
//    val price: String,
//    val imageUrl: String
//){
//    constructor():this("","", 0,"","")
//}

