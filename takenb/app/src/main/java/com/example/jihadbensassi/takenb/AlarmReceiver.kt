package com.example.jihadbensassi.takenb

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity

/**
 * Created by jihadbensassi on 26/06/2018.
 */
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        /////////////////////////////NOTIF/////////////////////////////////////////////
        val notifIntent = Intent(context, MainActivity::class.java)
        val notifPendingIntent = PendingIntent.getActivity(
                context, 52, notifIntent, PendingIntent.FLAG_IMMUTABLE)
        val notif = NotificationCompat.Builder(context, "msg")
                .setSmallIcon(R.mipmap.takenb)
                .setContentTitle("Paiement")
                .setContentText("Votre paiement à bien était éffectuer, pour suivre vos achat, veuillez aller dans la rubrique 'suivi'")
                .setContentIntent(notifPendingIntent)
                .setAutoCancel(true)
        val notificationId = 1
        val mgr = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE)
                as NotificationManager
        mgr.notify(notificationId, notif.build())
    }
}