package org.libreoffice.androidapp.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics

class App : Application(),ActivityLifecycleCallbacks, ViewModelStoreOwner {
    private lateinit var appViewModelStore: ViewModelStore
    override val viewModelStore: ViewModelStore
        get() = appViewModelStore

    companion object {
        private var instance: App? = null
        lateinit var mFirebaseAnalytics: FirebaseAnalytics

        @JvmStatic
        fun initROAS(revenue: Long, currency: String) {
            try {
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(instance)
                val editor: SharedPreferences.Editor = sharedPref.edit()
                val currentImpressionRevenue = revenue / 1000000
                // make sure to divide by 10^6
                val previousTroasCache: Float = sharedPref.getFloat(
                    "TroasCache",
                    0F
                ) //Use App Local storage to store cache of tROAS
                val currentTroasCache = (previousTroasCache + currentImpressionRevenue).toFloat()
                //check whether to trigger  tROAS event
                if (currentTroasCache >= 0.01) {
                    LogTroasFirebaseAdRevenueEvent(currentTroasCache, currency)
                    editor.putFloat("TroasCache", 0f) //reset TroasCache
                } else {
                    editor.putFloat("TroasCache", currentTroasCache)
                }
                editor.commit()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        private fun LogTroasFirebaseAdRevenueEvent(tRoasCache: Float, currency: String) {
            try {
                val bundle = Bundle()
                bundle.putDouble(
                    FirebaseAnalytics.Param.VALUE,
                    tRoasCache.toDouble()
                ) //(Required)tROAS event must include Double Value
                bundle.putString(
                    FirebaseAnalytics.Param.CURRENCY,
                    currency
                ) //put in the correct currency
                mFirebaseAnalytics.logEvent("Daily_Ads_Revenue", bundle)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }



    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate() {
        super.onCreate()
        instance = this
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        initAdjust()
        registerActivityLifecycleCallbacks(this)
    }

    private fun initAdjust() {
        val appToken = "e448kz7stuyo"
        val environment =
            if (BuildConfig.DEBUG) AdjustConfig.ENVIRONMENT_SANDBOX else AdjustConfig.ENVIRONMENT_PRODUCTION
        val config = AdjustConfig(this, appToken, environment)
        Adjust.onCreate(config)
    }


    override fun onActivityStarted(p0: Activity) {

    }

    override fun onActivityResumed(p0: Activity) {
        Adjust.onResume()
    }

    override fun onActivityPaused(p0: Activity) {
        Adjust.onPause()
    }

    override fun onActivityStopped(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {

    }

    override fun onActivityDestroyed(p0: Activity) {

    }

}