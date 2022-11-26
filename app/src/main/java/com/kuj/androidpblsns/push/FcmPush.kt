package com.kuj.androidpblsns.push

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class FcmPush {
    var JSON = MediaType.parse("application/json; charset=utf-8")
    var url = "https://fcm.googleapis.com/fcm/send"
    var serverKey = "AAAAO3h4pHc:APA91bHFneR7QCbD8r_CBh31fN-eLuBLQH2YqWnu7409Izdyi77LJH71UlgNZC7k6Umi-ouaVWcXcVP3vbFfbTrwdAzLeVzKEwPKrj4TvHVZBoruIzK7j-tIyymPO3w4udtiLWxwii-C"
    var gson: Gson? = null
    var okHttpClient: OkHttpClient? = null
    val database = Firebase.database
    var dbRef = database.getReference("user")

    companion object {
        var instance = FcmPush()
    }

    init {
        gson = Gson()
        okHttpClient = OkHttpClient()
    }

    fun sendMessage(destinationUid: String, title: String, message: String) {
        FirebaseFirestore.getInstance().collection("pushtokens").document(destinationUid).get()
            .addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                var token = task?.result?.get("pushToken").toString()
                Log.v("####token", token)
                var pushDTO = PushDTO()
                pushDTO.to = token
                pushDTO.notification.title = title
                pushDTO.notification.body = message

                var body = RequestBody.create(JSON, gson!!.toJson(pushDTO))
                var request = Request.Builder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "key=" + serverKey)
                    .url(url)
                    .post(body)
                    .build()
                okHttpClient?.newCall(request)?.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.v("result", "fail")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Log.v("result", "success")
                        println(response.body()?.string())
                        Log.v("result", "이거임")
                    }
                })
            }

            }
    }
}


