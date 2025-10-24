package org.libreoffice.androidlib.ads

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresPermission
import androidx.core.view.children
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import org.libreoffice.androidlib.manager.AdRevenueManager
import org.libreoffice.androidlib.R
import org.libreoffice.androidlib.manager.DocumentManager.logD
import org.libreoffice.androidlib.utils.Preferences.bannerAdsId
import org.libreoffice.androidlib.utils.Preferences.showAds

@SuppressLint("StaticFieldLeak")
object BannerAds {

    fun getAdSize(activity: Activity): AdSize {
        val display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val adWidth = (outMetrics.widthPixels / outMetrics.density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
    }


    @RequiresPermission(Manifest.permission.INTERNET)
    fun initBannerAds(ctx: Activity, adBanner: ViewGroup?) {
        try {
            if (!showAds) {
                adBanner?.visibility = View.GONE
                return
            }

            if (adBanner == null) {
                return
            }
            val adViewContainer: LinearLayout = adBanner.findViewById(R.id.adView_container) ?: return
            val mAdViewBanner = AdView(ctx)
            mAdViewBanner.setAdSize(getAdSize(ctx))
            mAdViewBanner.adUnitId = bannerAdsId
            val adRequest = if (false) {
                val extras = Bundle().apply { putString("collapsible", "bottom") }
                AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter::class.java, extras).build()
            } else {
                AdRequest.Builder().build()
            }

            adViewContainer.removeAllViews()
            adViewContainer.addView(mAdViewBanner)
            mAdViewBanner.loadAd(adRequest)
            mAdViewBanner.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    mAdViewBanner.setOnPaidEventListener { adValue ->
                        logD("TANHXXXX =>>>>> adValue BannerAds:${adValue}")
                        try {
                            AdRevenueManager.sendAdRevenue(ctx, adValue)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    adViewContainer.visibility = View.VISIBLE
                    hideBannerLoading(adBanner, false)
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    logD("TANHXXXX =>>>>> onAdFailedToLoad: ${loadAdError.message}")
                    adBanner.visibility = View.GONE
                }

                override fun onAdClicked() {
                    adViewContainer.visibility = View.GONE
                    hideBannerLoading(adBanner, true)
                }
            }

            hideBannerLoading(adBanner, false)
        } catch (e: Exception) {
            logD("TANHXXXX =>>>>> exception: ${e.message}")
            e.printStackTrace()
        }
    }

    fun hideBannerLoading(bannerAd: ViewGroup, bl: Boolean) {
        try {

            bannerAd.children.forEach {
                if (it is ShimmerFrameLayout) {
                    Log.e("=4412ds=", "hideBannerLoading: ")
                    it.visibility = if (bl) View.GONE else View.VISIBLE
                }
            }

            bannerAd.findViewById<View>(R.id.view_d).visibility = if (bl) View.GONE else View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}