package org.libreoffice.androidlib.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object Preferences {
    private const val PREF_BANNER_ADS_ID = "pref_banner_ads_id"
    private const val PREF_SHOW_ADS = "pref_show_ads"

    private const val DEFAULT_BANNER_ADS_ID = "ca-app-pub-3940256099942544/9214589741"
    private const val DEFAULT_SHOW_ADS = true

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    }

    var bannerAdsId: String
        get() = preferences.getString(PREF_BANNER_ADS_ID, DEFAULT_BANNER_ADS_ID) ?: DEFAULT_BANNER_ADS_ID
        set(value) = preferences.edit().putString(PREF_BANNER_ADS_ID, value).apply()

    var showAds: Boolean
        get() = preferences.getBoolean(PREF_SHOW_ADS, DEFAULT_SHOW_ADS)
        set(value) = preferences.edit().putBoolean(PREF_SHOW_ADS, value).apply()
}