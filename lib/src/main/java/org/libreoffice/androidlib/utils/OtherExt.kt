package org.libreoffice.androidlib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.util.Log
import org.libreoffice.androidlib.BuildConfig
import org.libreoffice.androidlib.Intent_Killed_Process
import org.libreoffice.androidlib.LOActivity

object OtherExt {

    fun getIntentToEdit(activity: Activity, uri: Uri?): Intent {
        val i = Intent(Intent.ACTION_EDIT, uri)
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        val componentName =
            ComponentName(activity.getPackageName(), LOActivity::class.java.getName())
        i.setComponent(componentName)
        return i
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    fun Activity.registerCloseReceiver(
        action: String = Intent_Killed_Process,
        onClosed: (() -> Unit)? = null
    ) {
        val filter = IntentFilter(action)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                onClosed?.invoke()
                try {
                    unregisterReceiver(this)
                } catch (_: Exception) { }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("DEPRECATION")
            registerReceiver(receiver, filter)
        }
    }


    fun Any.logD(log: String) {
        if (BuildConfig.DEBUG) {
            Log.d(this::class.java.simpleName, log)
        }
    }
}