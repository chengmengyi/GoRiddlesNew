package game.riddles.core.top

import android.app.Application
import androidx.annotation.CallSuper
import com.blankj.utilcode.util.ProcessUtils
import game.riddles.core.event.TEvent
import game.riddles.core.utils.FirebaseUtils
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

abstract class BaseApplication : Application() {

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        initVpnCore()
        if (ProcessUtils.isMainProcess()) {
            onMainProcessCreate()
        }
    }

    abstract fun initVpnCore()

    @CallSuper
    protected open fun onMainProcessCreate() {
        FirebaseUtils.init(this)
        initTEvents()
    }

    private fun initTEvents() {
        MainScope().launch {
            TEvent(this@BaseApplication).transSession()
            TEvent(this@BaseApplication).transInstall()
        }
    }
}