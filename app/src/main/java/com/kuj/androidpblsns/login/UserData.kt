package com.kuj.androidpblsns.login

data class UserData(
    var nickname: String,
    var email : String,
    var uid : String,
    var following:HashMap<String,Boolean>?,
    var followingCnt:Int
){
    constructor(): this("", "", "", HashMap(),0)
}