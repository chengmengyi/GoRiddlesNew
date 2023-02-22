package game.riddles.core.utils

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter

object DataBindingUtils {
    @JvmStatic
    @BindingAdapter("image_res_id")
    fun bindImageResId(iv: ImageView, res: Int) {
        if (res > 0) {
            iv.setImageResource(res)
        }
    }

    @JvmStatic
    @BindingAdapter("image_bitmap")
    fun bindImageBitmap(iv: ImageView, bitmap: Bitmap?) {
        iv.setImageBitmap(bitmap)
    }

    @JvmStatic
    @BindingAdapter("is_enabled")
    fun setEnable(v: View, enable: Boolean) {
        v.isEnabled = enable
    }

    @JvmStatic
    @BindingAdapter("background_res_id")
    fun setBackgroundResId(iv: ImageView, res: Int) {
        if (res > 0) {
            iv.setBackgroundResource(res)
        }
    }
}