package com.jjhadr.fast3_chapter01

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private val resultTextView : TextView by lazy {
        findViewById(R.id.resultTextView)
    }
    private val firebaseToken:TextView by lazy {
        findViewById(R.id.firebaseTokenTextView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFirebase()
        updateResult()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        setIntent(intent)
        updateResult(true)
    }
    private fun initFirebase() {
        //파이어베이스 메시지 토큰
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            // 연결되었을때
            if(task.isSuccessful) {
                val token = task.result
                firebaseToken.text= token
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateResult(isNewIntent : Boolean = false) {
        resultTextView.text = (intent.getStringExtra("notificationType") ?: "앱 런처") +
                if (isNewIntent) {
                    "(으)로 갱신했습니다."
                } else {
                    "(으)로 실행했습니다."
                }
    }

}