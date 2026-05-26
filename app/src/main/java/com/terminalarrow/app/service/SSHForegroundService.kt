package com.terminalarrow.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.terminalarrow.app.MainActivity
import com.terminalarrow.app.R

/**
 * Lightweight foreground service that keeps long-running SSH connections
 * alive when the app goes to the background. Every entry point is
 * defensively wrapped so an exception here can never crash the host app.
 */
class SSHForegroundService : Service() {

    private var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate() {
        super.onCreate()
        runCatching {
            val manager = getSystemService(NotificationManager::class.java)
            if (manager != null && manager.getNotificationChannel(CHANNEL_ID) == null) {
                manager.createNotificationChannel(
                    NotificationChannel(
                        CHANNEL_ID,
                        getString(R.string.notification_channel_ssh),
                        NotificationManager.IMPORTANCE_LOW
                    ).apply {
                        setShowBadge(false)
                        description = getString(R.string.notification_text)
                    }
                )
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            val notification = buildNotification()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
            acquireWakeLock()
        } catch (t: Throwable) {
            Log.w(TAG, "Failed to start foreground service", t)
            stopSelf()
        }
        return START_STICKY
    }

    private fun buildNotification(): android.app.Notification {
        val tapIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or
            (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        val tapPending = PendingIntent.getActivity(this, 0, tapIntent, pendingFlags)
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setOngoing(true)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(tapPending)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun acquireWakeLock() {
        if (wakeLock?.isHeld == true) return
        runCatching {
            val pm = getSystemService(Context.POWER_SERVICE) as? PowerManager
            wakeLock = pm?.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_TAG)?.apply {
                setReferenceCounted(false)
                // Cap the wake-lock to one hour; the service will renew it on next start.
                acquire(60L * 60L * 1000L)
            }
        }
    }

    override fun onDestroy() {
        runCatching {
            if (wakeLock?.isHeld == true) wakeLock?.release()
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val TAG = "SSHForegroundService"
        private const val CHANNEL_ID = "ssh_session"
        private const val NOTIFICATION_ID = 1
        private const val WAKE_TAG = "TerminalArrow::SSH"

        fun start(context: Context) {
            runCatching {
                val intent = Intent(context, SSHForegroundService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            }
        }

        fun stop(context: Context) {
            runCatching { context.stopService(Intent(context, SSHForegroundService::class.java)) }
        }
    }
}
