package game.riddles.browser.tab

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import androidx.core.net.toUri
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.IntentUtils
import game.riddles.browser.R
import game.riddles.browser.view.input.SearchInputActivity
import game.riddles.core.utils.adaptStatusBar
import game.riddles.core.utils.applySingleDenouncingClick
import game.riddles.core.utils.layout2View

internal class TabHomeView(private val context: Context) : ITabHomeView {
    private lateinit var myContentView: View
    private lateinit var captureView: View

    override fun onCreate() {
        myContentView = context.layout2View(R.layout.layout_tab_home)
        myContentView.findViewById<View>(R.id.top_layout).adaptStatusBar()
        myContentView.findViewById<View>(R.id.btn_back).applySingleDenouncingClick {
            (it.context as? Activity)?.onBackPressed()
        }
        myContentView.findViewById<View>(R.id.btn_google).applySingleDenouncingClick {
            startOtherBrowser(it.context, "com.android.chrome")
        }
        myContentView.findViewById<View>(R.id.btn_edge).applySingleDenouncingClick {
            startOtherBrowser(it.context, "com.microsoft.emmx")
        }
        myContentView.findViewById<View>(R.id.btn_firefox).applySingleDenouncingClick {
            startOtherBrowser(it.context, "org.mozilla.firefox")
        }
        myContentView.findViewById<View>(R.id.btn_opera).applySingleDenouncingClick {
            startOtherBrowser(it.context, "com.opera.browser")
        }
        myContentView.findViewById<View>(R.id.btn_start_search).applySingleDenouncingClick {
            ActivityUtils.startActivity(Intent(it.context, SearchInputActivity::class.java), 0, 0)
        }
        captureView = myContentView.findViewById(R.id.capture_rect_view)
    }

    private fun startOtherBrowser(context: Context, packageName: String) {
        runCatching {
            context.startActivity(IntentUtils.getLaunchAppIntent(packageName))
        }
            .onFailure {
                runCatching {
                    context.startActivity(Intent(
                        Intent.ACTION_VIEW,
                        "https://play.google.com/store/apps/details?id=${packageName}".toUri()
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }
            }
    }

    override fun getContentView(): View {
        return myContentView
    }

    override fun getCaptureBitmap(): Bitmap? {
        var cacheHomeCapture = WebCaptureCacheFactory.getWebHomeCapture()
        if (cacheHomeCapture == null) {
            val newCapture = ImageUtils.view2Bitmap(captureView)
            cacheHomeCapture = newCapture
            WebCaptureCacheFactory.updateWebHomeCapture(newCapture)
        }
        return cacheHomeCapture
    }
}