package game.riddles.ad.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import game.riddles.ad.apis.SceneAdsApi
import game.riddles.ad.factory.newAdSdk
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal object AppHotUtils {
    private var startedActivities = 0
    private var backgroundJob: Job? = null
    private var needExecBackgroundTask = false
    private val backgroundTasks = mutableSetOf<Runnable>()

    fun registerTask(runnable: Runnable) {
        backgroundTasks.add(runnable)
    }

    fun unregisterTask(runnable: Runnable) {
        backgroundTasks.remove(runnable)
    }

    fun init(app: Application) {
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {
                startedActivities++
                backgroundJob?.cancel()
                backgroundJob = null
                if (needExecBackgroundTask) {
                    onHotForeground()
                }
            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {
                startedActivities--
                if (startedActivities <= 0) {
                    onHotBackground()
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }

        })
    }

    private fun onHotForeground() {
        if (ActivityUtils.getActivityList().isNotEmpty()) {
            ActivityUtils.getTopActivity()?.let {
                SceneAdsApi.funProxy.proxyAdStartLaunchActivity(it)
            }
        }
        val it = backgroundTasks.iterator()
        while (it.hasNext()) {
            it.next().run()
        }
        needExecBackgroundTask = false
    }

    private fun onHotBackground() {
        backgroundJob = GlobalScope.launch {
            delay(3000L)
            needExecBackgroundTask = true
            SceneAdsApi.funProxy.proxyAdFinishLaunchActivity()
            newAdSdk().finishAdActivity()
        }
    }
}