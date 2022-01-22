package com.jjhadr.fast3_chapter01

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

//서비스이기에 Manifests 등록
class MyFirebaseMessagingService : FirebaseMessagingService() {
    //파이어베이스 메시징서비스를 사용하려면  onNewToken을 재정의하여야 한다.
    //토큰이 갱신될 때마다 서버에 해당 토큰을 갱신해주는 처리를 위한 것
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }
    //실제로 파이어베이스 클라우드 메시지를 수신할 때마다 호출해줌
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        createNotificationChannel()

        val type = remoteMessage.data["type"]
            ?.let { NotificationType.valueOf(it) }
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]

        type ?: return

        NotificationManagerCompat.from(this)
            .notify(type.id,createNotification(type,title,message)) // id , builder

    }
    //Notification : Notification 은 애플리케이션과 별도로 관리되는 메시지이다.
    //Notification 메시지를 OS 에게 요청하면 OS는 알림 창 영역에 알림 메시지를 표시한다.
    //화면을 가지지 않는 실행단위에서 메시지를 표시할 때 주로 사용한다.
    private fun createNotificationChannel() {
            //오레오버전 이상일 때
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNDL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT) //id 이름 중요도
            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel) //채널만들기
        }
    }

    private fun createNotification(
        type: NotificationType,
        title: String?,
        message: String?) : Notification {
        val intent = Intent(this,MainActivity::class.java).apply {
            putExtra("notificationType","${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) // 같은 엑티비티는 재사용
        }
        //Intent : 엑티비티 호출 관련
        //pendingIntent : 인텐트를 다룰 수 있는 권한을 주는 것
        //메시지가 5개 왔을 때 인텐트가 같은 값이기 때문에 동일한 펜딩 인텐트 값을 줌
        val pendingIntent = PendingIntent.getActivity(this,type.id, intent, FLAG_UPDATE_CURRENT)

       val notificationBuilder =  NotificationCompat.Builder(this, CHANNDL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24) //아이콘
            .setContentTitle(title)
            .setContentText(message).setPriority(NotificationCompat.PRIORITY_DEFAULT)
           .setContentIntent(pendingIntent)
           .setAutoCancel(true) // 클릭시 메시지 없애줌

        when (type) {
            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> {
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("@@#%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +
                                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
                )
            }
            NotificationType.CUSTOM -> {

                notificationBuilder.setStyle(
                    NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(
                            packageName,
                            R.layout.view_custom_notification
                        ).apply {
                            setTextViewText(R.id.title,title) //뒤에 타이틀은은 createNotification에서 지정한것
                            setTextViewText(R.id.message,message)
                       }
                    )
            }
        }
        return notificationBuilder.build()

    }
    companion object {
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Pary를 위한 채널널"
        private const val CHANNDL_ID ="Channel Id"
    }
}