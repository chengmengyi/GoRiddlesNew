package game.riddles.core.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.BarUtils

fun ViewGroup.addViewFromLayout(
    layoutResId: Int,
    params: ViewGroup.LayoutParams = newMatchParentLayoutParam(),
    attachToRoot: Boolean = false
): View {
    val child = LayoutInflater.from(context).inflate(layoutResId, this, attachToRoot)
    addView(child, params)
    return child
}

fun ViewGroup.inflateView(layoutResId: Int): View {
    return context.layout2View(layoutResId, root = this)
}

fun <Binding : ViewDataBinding> ViewGroup.bindingView(layoutResId: Int): Binding {
    return context.layout2Binging(layoutResId, this)
}

fun newMatchParentLayoutParam(): ViewGroup.LayoutParams {
    return ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
}

fun Context.layout2View(layoutResId: Int, root: ViewGroup? = null): View {
    return LayoutInflater.from(this).inflate(layoutResId, root, false)
}

fun <Binding : ViewDataBinding> Context.layout2Binging(
    layoutResId: Int,
    root: ViewGroup? = null
): Binding {
    return DataBindingUtil.inflate(LayoutInflater.from(this), layoutResId, root, false)
}

fun View.adaptStatusBar() {
    BarUtils.addMarginTopEqualStatusBarHeight(this)
}

fun View.removeFromParent() {
    (parent as? ViewGroup)?.removeView(this)
}