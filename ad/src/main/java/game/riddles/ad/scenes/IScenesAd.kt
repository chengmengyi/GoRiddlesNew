package game.riddles.ad.scenes

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

internal interface IScenesAd {
    fun load(context: Context)
    fun show(context: AppCompatActivity, data: Any, callback: OnSceneAdShowCallback)

    interface OnSceneAdShowCallback {
        fun onAdNotSupported() {}
        fun onAdShowCompleted() {}
        fun onAdReward() {}
    }
}