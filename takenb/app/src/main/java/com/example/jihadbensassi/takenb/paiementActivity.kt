package com.example.jihadbensassi.takenb

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_delete.*
import org.jetbrains.anko.db.asMapSequence
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask

/**
 * Created by jihadbensassi on 26/06/2018.
 */
class paiementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paiement)
        val mgr = getSystemService(Context.ALARM_SERVICE)
                as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(
                this, 52, intent, 0)
        mgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                alarmIntent)

        ////////////////////// NOTIFICATION POUR OREO /////////////////////////
        if (Build.VERSION.SDK_INT >= 26) {
            val mgr = getSystemService(
                    NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                    "msg", "Message",
                    NotificationManager.IMPORTANCE_DEFAULT)
            mgr . createNotificationChannel (channel)
        }


    }

}
