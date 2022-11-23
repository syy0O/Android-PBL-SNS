package com.kuj.androidpblsns.alarm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FBMessaging : FirebaseMessagingService() {

    //디바이스마다 FCM(Firebase Cloud Messaging) 토큰이 할당됨, 고유한 값으로 특정 디바이스에만 메시지를 보낼 때 사용함
    //이 토큰이 변경될 때 호출되는 메소드임
    //앱이 재설치 되거나 할 때 변경됨
    override fun onNewToken(token: String) { // Get updated InstanceID token.
        Log.d(TAG, "Refreshed token: $token")

        // TODO: Implement this method to send any registration to your app's servers.
        // sendRegistrationToServer(token)
    }

    //FCM 메시지를 받을 때, 앱이 현재 전면에서 실행 중이면 이 메소드가 호출됨
    //앱이 실행 중이 아니거나 백그라운드 실행 중이면 알림(notification)이 표시됨
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")
        Log.d(TAG, "Message data payload: ${remoteMessage.data}")
        val msgBody = remoteMessage.notification?.body
        Log.d(TAG, "Message Notification Body: $msgBody")
    }

    companion object {
        const val TAG = "MyFirebaseMessaging"
    }
}