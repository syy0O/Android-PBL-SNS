package com.kuj.androidpblsns.login

data class UserData(
    var nickname: String,
    var email : String,
    var uid : String
){
    constructor(): this("", "", "")
}