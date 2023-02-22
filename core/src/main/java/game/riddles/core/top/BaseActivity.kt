package game.riddles.core.top

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.BarUtils
import game.riddles.core.top.bridge.IViewBridge
import game.riddles.core.utils.forEachRemain
import game.riddles.core.utils.isOnDestroyed

abstract class BaseActivity<Binding : ViewDataBinding, ViewData : BaseViewData> :
    AppCompatActivity() {
    protected lateinit var binding: Binding
        private set
    protected abstract val viewData: ViewData?
    private val layoutLifecycleObservers = mutableSetOf<ILayoutLifecycleObserver>()
    private val viewBridge by lazy {
        object : IViewBridge {
            override fun addLayoutLifecycleObserver(observer: ILayoutLifecycleObserver) {
                this@BaseActivity.addLayoutLifecycleObserver(observer)
            }

            override fun removeLayoutLifecycleObserver(observer: ILayoutLifecycleObserver) {
                this@BaseActivity.removeLayoutLifecycleObserver(observer)
            }

            override fun getBridgeContext(): Context? {
                val that = this@BaseActivity
                return if (that.lifecycle.isOnDestroyed()) null else that
            }
        }
    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adaptScreenByHeightPixels()
        BarUtils.setStatusBarLightMode(this, true)
        val that = this
        binding = DataBindingUtil.setContentView<Binding>(this, getImplLayoutResId()).apply {
            this.lifecycleOwner = that
            this.setVariable(1, viewData)
        }
        onInitView()
        (viewData as? ILayoutLifecycleObserver)?.let {
            addLayoutLifecycleObserver(it)
        }
        viewData?.setViewBridge(viewBridge)
        layoutLifecycleObservers.iterator().forEachRemain { it.onLayoutCreate(savedInstanceState) }
    }

    override fun onStart() {
        super.onStart()
        layoutLifecycleObservers.iterator().forEachRemain { it.onLayoutStart() }
    }

    override fun onPostResume() {
        super.onPostResume()
        layoutLifecycleObservers.iterator().forEachRemain { it.onLayoutResume() }
    }

    override fun onPause() {
        super.onPause()
        layoutLifecycleObservers.iterator().forEachRemain { it.onLayoutPause() }
    }

    override fun onStop() {
        super.onStop()
        layoutLifecycleObservers.iterator().forEachRemain { it.onLayoutStop() }
    }

    override fun onDestroy() {
        super.onDestroy()
        layoutLifecycleObservers.iterator().forEachRemain { it.onLayoutDestroy() }
        layoutLifecycleObservers.clear()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        layoutLifecycleObservers.iterator().forEachRemain { it.onLayoutNewIntent(intent) }
    }

    private fun adaptScreenByHeightPixels() {
        resources.displayMetrics.run {
            density = heightPixels / 800.0F
            densityDpi = (160 * density).toInt()
        }
    }

    fun addLayoutLifecycleObserver(observer: ILayoutLifecycleObserver) {
        layoutLifecycleObservers.add(observer)
    }

    fun removeLayoutLifecycleObserver(observer: ILayoutLifecycleObserver) {
        layoutLifecycleObservers.remove(observer)
    }

    protected abstract fun onInitView()

    protected abstract fun getImplLayoutResId(): Int
}