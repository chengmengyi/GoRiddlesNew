package game.riddles.core.top

import android.content.Context
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lxj.xpopup.core.CenterPopupView

abstract class BaseCenterDialog<Binding : ViewDataBinding, ViewData : BaseViewDataNonComp>(context: Context) :
    CenterPopupView(context) {
    protected lateinit var binding: Binding
    protected lateinit var viewData: ViewData

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        val that = this
        viewData = newViewData()
        binding = DataBindingUtil.bind<Binding>(popupImplView)!!.apply {
            this.lifecycleOwner = that
            this.setVariable(1, viewData)
        }
    }

    protected abstract fun newViewData(): ViewData

    @CallSuper
    override fun onDismiss() {
        super.onDismiss()
        viewData.onCleared()
    }
}