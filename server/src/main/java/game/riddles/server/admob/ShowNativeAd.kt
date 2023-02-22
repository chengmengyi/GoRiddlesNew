package game.riddles.server.admob

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.blankj.utilcode.util.SizeUtils
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import game.riddles.server.R
import game.riddles.server.base.BaseAc
import game.riddles.server.util.AdShowed
import game.riddles.server.util.logGo
import game.riddles.server.util.show
import kotlinx.coroutines.*

class ShowNativeAd(
    private val type:String,
    private val baseAc: BaseAc
) {
    private var last:NativeAd?=null
    private var showJob:Job?=null

    fun show(showDesc:Boolean=true){
        LoadAdImpl.load(type)
        endShow()
        showJob= GlobalScope.launch(Dispatchers.Main) {
            delay(300L)
            if (!baseAc.resume){
                return@launch
            }
            while (true) {
                if (!isActive) {
                    break
                }

                val adResult = LoadAdImpl.getAdResult(type)
                if(baseAc.resume && null!=adResult && adResult is NativeAd){
                    cancel()
                    last?.destroy()
                    last=adResult
                    show(adResult,showDesc)
                }

                delay(1000L)
            }
        }
    }

    private fun show(adResult:NativeAd,showDesc:Boolean){
        logGo("start show $type ad")
        val viewNative = baseAc.findViewById<NativeAdView>(R.id.native_view)
        viewNative.iconView=baseAc.findViewById(R.id.native_logo)
        (viewNative.iconView as ImageFilterView).setImageDrawable(adResult.icon?.drawable)

        viewNative.callToActionView=baseAc.findViewById(R.id.native_install)
        (viewNative.callToActionView as AppCompatTextView).text=adResult.callToAction

        viewNative.mediaView=baseAc.findViewById(R.id.native_media)
        adResult.mediaContent?.let {
            viewNative.mediaView?.apply {
                setMediaContent(it)
                setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                outlineProvider = object : ViewOutlineProvider() {
                    override fun getOutline(view: View?, outline: Outline?) {
                        if (view == null || outline == null) return
                        outline.setRoundRect(
                            0,
                            0,
                            view.width,
                            view.height,
                            SizeUtils.dp2px(8F).toFloat()
                        )
                        view.clipToOutline = true
                    }
                }
            }
        }

        if (showDesc){
            viewNative.bodyView=baseAc.findViewById(R.id.native_desc)
            (viewNative.bodyView as AppCompatTextView).text=adResult.body
        }


        viewNative.headlineView=baseAc.findViewById(R.id.native_title)
        (viewNative.headlineView as AppCompatTextView).text=adResult.headline

        viewNative.setNativeAd(adResult)
        baseAc.findViewById<AppCompatImageView>(R.id.iv_native_cover).show(false)

        AdShowed.addShow()
        LoadAdImpl.removeAd(type)
        LoadAdImpl.load(type)
        AdShowed.setShowed(type,true)
    }

    fun endShow(){
        showJob?.cancel()
        showJob=null
    }
}