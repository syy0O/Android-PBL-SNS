package com.kuj.androidpblsns.data

data class UserData(
    var nickname: String = "",
    var email : String = "",
    var uid : String = "",
    var follower: List<String> = emptyList(),
    val followerCnt: Int = 0,
    var following: List<String> = emptyList(),
    var followingCnt: Int = 0
) {
    init {
        val list = mutableListOf<String>()
        list.add("don't touch this key")
        follower = list.toList()
        following = list.toList()
    }
}