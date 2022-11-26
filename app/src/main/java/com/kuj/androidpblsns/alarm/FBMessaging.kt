package com.kuj.androidpblsns.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kuj.androidpblsns.HomeActivity
import com.kuj.androidpblsns.login.MainActivity

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
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification != null){
            sendNotification(remoteMessage)
        }else{
            Log.d(TAG, "수신 에러: Notification이 비어있습니다.")
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val id = 0
        var title = remoteMessage.notification!!.title
        var body = remoteMessage.notification!!.body

        var intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = "Chanel ID"
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_HIGH)

        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(id, notificationBuilder.build())
    }

    companion object {
        const val TAG = "MyFirebaseMessaging"
    }
}