package game.riddles.core.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Picture

fun Picture?.toBitmap(
    width: Int = -1,
    height: Int = -1
): Bitmap? {
    this ?: return null
    return try {
        val w = if (width == -1) this.width else width
        val h = if (height == -1) this.height else height
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        draw(canvas)
        bitmap
    } catch (e: Exception) {
        null
    }
}