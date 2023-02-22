package game.riddles.core.utils

import android.app.Application
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import game.riddles.core.BuildConfig

object FirebaseUtils {
    var adConfig by CacheableStringDelegate { "AD_CONFIG" }
        private set

    internal fun init(app: Application) {
        if (BuildConfig.DEBUG) return
        Firebase.remoteConfig
            .apply {
                setConfigSettingsAsync(
                    FirebaseRemoteConfigSettings.Builder()
                        .setMinimumFetchIntervalInSeconds(
                            if (BuildConfig.DEBUG) 5L else 3600L
                        )
                        .build()
                )
            }
            .fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val remoteConfig = Firebase.remoteConfig
                    adConfig = remoteConfig.getString("riddles_ad")
                }
            }
    }
}