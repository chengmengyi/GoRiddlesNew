package game.riddles.core.top

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import game.riddles.core.top.bridge.IViewBridge

abstract class BaseViewData(application: Application) : AndroidViewModel(application) {
    protected var mViewBridge: IViewBridge? = null
        private set

    fun setViewBridge(bridge: IViewBridge) {
        mViewBridge = bridge
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        mViewBridge = null
    }
}