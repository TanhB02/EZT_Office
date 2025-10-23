package org.libreoffice.androidlib.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdValue
import org.libreoffice.androidlib.BuildConfig
import org.libreoffice.androidlib.utils.DocumentCallback

object AdRevenueManager {
    private const val TAG = "AdRevenueManager"
    private const val ACTION_AD_REVENUE = "org.libreoffice.androidlib.AD_REVENUE"
    private const val EXTRA_VALUE_MICROS = "valueMicros"
    private const val EXTRA_CURRENCY_CODE = "currencyCode"
    private const val EXTRA_PRECISION_TYPE = "precisionType"
    private const val DEFAULT_CURRENCY = "USD"

    private var callback: DocumentCallback? = null
    private var receiver: BroadcastReceiver? = null


    @JvmStatic
    fun registerCallback(context: Context, callback: DocumentCallback) {
        unregisterCallback(context)
        this.callback = callback
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action != ACTION_AD_REVENUE) return
                val valueMicros = intent.getLongExtra(EXTRA_VALUE_MICROS, 0L)
                val currencyCode = intent.getStringExtra(EXTRA_CURRENCY_CODE) ?: DEFAULT_CURRENCY
                val precisionType = intent.getIntExtra(EXTRA_PRECISION_TYPE, AdValue.PrecisionType.UNKNOWN)
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Received ad revenue: $valueMicros $currencyCode (precision: $precisionType)")
                }
                callback.onAdRevenueReceived(valueMicros, currencyCode, precisionType)
            }
        }

        val filter = IntentFilter(ACTION_AD_REVENUE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Ad revenue callback registered")
        }
    }


    @JvmStatic
    fun unregisterCallback(context: Context) {
        receiver?.let {
            try {
                context.unregisterReceiver(it)
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Ad revenue callback unregistered")
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error unregistering receiver: ${e.message}")
            }
        }
        receiver = null
        callback = null
    }

    @JvmStatic
    fun sendAdRevenue(context: Context, adValue: AdValue) {
        try {
            val intent = Intent(ACTION_AD_REVENUE).apply {
                setPackage(context.packageName)
                putExtra(EXTRA_VALUE_MICROS, adValue.valueMicros)
                putExtra(EXTRA_CURRENCY_CODE, adValue.currencyCode)
                putExtra(EXTRA_PRECISION_TYPE, adValue.precisionType)
            }

            context.sendBroadcast(intent)

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Sent ad revenue: ${adValue.valueMicros} ${adValue.currencyCode}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending ad revenue", e)
        }
    }
}