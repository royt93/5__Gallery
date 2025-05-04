package com.mckimquyen.gallery.act

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Display
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.mckimquyen.gallery.BuildConfig
import com.mckimquyen.gallery.R
import com.mckimquyen.gallery.ext.config
import com.mckimquyen.gallery.ext.favoritesDB
import com.mckimquyen.gallery.ext.getFavoriteFromPath
import com.mckimquyen.gallery.ext.mediaDB
import com.mckimquyen.gallery.model.Favorite
import com.mckimquyen.gallery.sdkadbmob.AdMobManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.fossify.commons.activities.BaseSplashActivity
import org.fossify.commons.helpers.ensureBackgroundThread

class SplashAct : BaseSplashActivity() {
    override fun attachBaseContext(context: Context) {
        val override = Configuration(context.resources.configuration)
        override.fontScale = 1.0f
        applyOverrideConfiguration(override)
        super.attachBaseContext(context)
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            enableAdaptiveRefreshRate()
        }
    }

    private fun enableAdaptiveRefreshRate() {
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val display: Display? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display // Sử dụng API mới
        } else {
            @Suppress("DEPRECATION")
            wm.defaultDisplay // Fallback cho API thấp hơn
        }

        if (display != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val supportedModes = display.supportedModes
                val highestRefreshRateMode = supportedModes.maxByOrNull { it.refreshRate }
                if (highestRefreshRateMode != null) {
                    window.attributes = window.attributes.apply {
                        preferredDisplayModeId = highestRefreshRateMode.modeId
                    }
                    println("Adaptive refresh rate applied: ${highestRefreshRateMode.refreshRate} Hz")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun initActivity() {
        AdMobManager.loadAppOpenAd(
            context = this@SplashAct,
            adUnitId = BuildConfig.ADMOB_APP_OPEN_ID,
            onAdLoaded = { result ->
                Log.d("roy93~", "onAdLoaded result $result")
                init()
                AdMobManager.showAppOpenAd(this@SplashAct)
            },
        )
//        lifecycleScope.launch {
//            var hasCalledGoToMain = false
//            val job = launch {
//                delay(3_000)
//                if (!hasCalledGoToMain) {
//                    hasCalledGoToMain = true
//                    Log.d("roy93~", "goToMain #1")
//                    init()
//                }
//            }
//            AdMobManager.loadAppOpenAd(
//                context = this@SplashAct,
//                adUnitId = BuildConfig.ADMOB_APP_OPEN_ID,
//                onAdLoaded = {
//                    if (!hasCalledGoToMain) {
//                        hasCalledGoToMain = true
//                        job.cancel()
//                        Log.d("roy93~", "goToMain #2")
//                        init()
//                        AdMobManager.showAppOpenAd(this@SplashAct)
//                    }
//                },
//            )
//        }
    }

    private fun init() {
        // check if previously selected favorite items have been properly migrated into the new Favorites table
        if (config.wereFavoritesMigrated) {
            goToMain()
        } else {
            if (config.appRunCount == 0) {
                config.wereFavoritesMigrated = true
                goToMain()
            } else {
                config.wereFavoritesMigrated = true
                ensureBackgroundThread {
                    val favorites = ArrayList<Favorite>()
                    val favoritePaths = mediaDB.getFavorites().map { it.path }.toMutableList() as ArrayList<String>
                    favoritePaths.forEach {
                        favorites.add(getFavoriteFromPath(it))
                    }
                    favoritesDB.insertAll(favorites)

                    runOnUiThread {
                        goToMain()
                    }
                }
            }
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainAct::class.java))
        finish()
    }
}
