package game.riddles

import android.animation.ValueAnimator
import android.app.Application
import androidx.lifecycle.MutableLiveData
import game.riddles.ad.apis.SceneAdsApi
import game.riddles.core.top.BaseViewData
import game.riddles.core.top.ILayoutLifecycleObserver
import game.riddles.core.utils.dismiss

class SplashData(application: Application) : BaseViewData(application), ILayoutLifecycleObserver {
    val progress = MutableLiveData(0)
    val startQuestion = MutableLiveData<Boolean>()
    val showOpenAd = MutableLiveData<Any>()

    private var mProgressVa: ValueAnimator = ValueAnimator.ofInt(0, 100)
    private var mSplashProgress = 0
        set(value) {
            field = value
            progress.value = value
        }

    fun startSplashProgress() {
        if (mSplashProgress > 0) return
        SceneAdsApi.loadOpen(getApplication())
        mProgressVa
            .apply {
                duration = 10000L
                addUpdateListener { animator ->
                    mSplashProgress = animator.animatedValue as Int
                    if (mSplashProgress in 20..99) {
                        SceneAdsApi.resultOpen()?.let {
                            mSplashProgress = 100
                            animator.dismiss()
                            showOpenAd.value = it
                        }
                    } else if (mSplashProgress >= 100) {
                        startQuestion.value = false
                    }
                }
            }
            .start()
    }

    fun onAdShowCompleted() {
        startQuestion.value = true
    }

    override fun onLayoutPause() {
        mProgressVa.pause()
    }

    override fun onLayoutResume() {
        mProgressVa.resume()
    }

    override fun onCleared() {
        super.onCleared()
        mProgressVa.dismiss()
    }
}