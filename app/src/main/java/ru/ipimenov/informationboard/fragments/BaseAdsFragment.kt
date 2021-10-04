package ru.ipimenov.informationboard.fragments

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import ru.ipimenov.informationboard.R

open class BaseAdsFragment : Fragment(), InterstitialAdClose {

    lateinit var adView: AdView
    var interstitialAd: InterstitialAd? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAds()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadInterstitialAd()
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }

    override fun onPause() {
        super.onPause()
        adView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        adView.destroy()
    }

    private fun initAds() {
        MobileAds.initialize(activity as Activity)
        var adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun loadInterstitialAd() {
        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context as Activity, getString(R.string.ad_interstitial_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interAd: InterstitialAd) {
                interstitialAd = interAd
            }
        })
    }

    fun showInterstitialAd() {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

                override fun onAdDismissedFullScreenContent() {
                    onClose()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    onClose()
                }
            }
            interstitialAd?.show(activity as Activity)
        } else {
            onClose()
        }
    }

    override fun onClose() {}
}