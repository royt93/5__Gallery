package com.mckimquyen.gallery

import android.app.Application
import android.util.Log
import com.github.ajalt.reprint.core.Reprint
import com.google.android.gms.ads.MobileAds
import com.mckimquyen.gallery.sdkadbmob.AdMobManager
import com.squareup.picasso.Downloader
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Request
import okhttp3.Response
import org.fossify.commons.extensions.checkUseEnglish

//TODO firebase analytic
//TODO why you see ad
//TODO UI ios switch

//120hz
//admob
//in app review bingo
//vung bi mat de show applovin config
//ad applovin
//font scale
//done mckimquyen
//rename app
//proguard
//leak canary
//gen ic_launcher https://easyappicon.com/
//license
//rate, more app, share app
//github
//keystore
//20 tester https://github.com/gj-loitp/20-TESTER-FOR-CLOSED-TESTING

class RApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupApp()
    }

    private fun setupApp() {
        checkUseEnglish()
        Reprint.initialize(this)
        Picasso.setSingletonInstance(Picasso.Builder(this).downloader(object : Downloader {
            override fun load(request: Request) = Response.Builder().build()

            override fun shutdown() {}
        }).build())
//        this.setupApplovinAd()
        setupAdmob()
    }

    private fun setupAdmob() {
        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(this@RApp) {}
            AdMobManager.init(this@RApp) { success, gaidCurrent ->
                Log.d("roy93~", "AdMobManager init success $success, gaidCurrent $gaidCurrent")
            }
        }
//        registerActivityLifecycleCallbacks(
//            AppLifecycleListener(
//                { isForeground, activity ->
//                    if (isForeground) {
//                        Log.d("roy93~", "App moved to Foreground")
//                        Log.d("roy93~", "activity.localClassName ${activity.localClassName}")
//                        Log.d(
//                            "roy93~",
//                            "SplashActivity::class.java.simpleName ${SplashAct::class.java.simpleName}"
//                        )
//                        if (activity.localClassName == SplashAct::class.java.simpleName) {
//                            //do nothing
//                        } else {
////                            AdMobManager.showAppOpenAd(activity)
//                        }
//                    } else {
//                        Log.d("roy93~", "App moved to Background")
//                    }
//                }, { activity ->
//                    Log.d("roy93~", "callbackActivityCreated ${activity.localClassName}")
//                    if (activity.localClassName == SplashAct::class.java.simpleName) {
//                        //do nothing
//                    } else {
////                        AdMobManager.loadAppOpenAd(
////                            context = this,
////                            adUnitId = BuildConfig.ADMOB_APP_OPEN_ID,
////                            onAdLoaded = {},
////                        )
//                    }
//                }
//            )
//        )
    }
}
