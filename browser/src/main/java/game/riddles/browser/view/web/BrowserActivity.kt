package game.riddles.browser.view.web

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import game.riddles.browser.R
import game.riddles.browser.databinding.ActivityBrowserBinding
import game.riddles.browser.view.tab.TabManageActivity
import game.riddles.core.top.BaseActivity
import game.riddles.core.utils.newMatchParentLayoutParam
import game.riddles.core.utils.removeFromParent

class BrowserActivity : BaseActivity<ActivityBrowserBinding, BrowserData>() {
    override val viewData: BrowserData by viewModels()

    override fun onInitView() {
        viewData.addOrRemoveTabContentView.observe(this) {
            addOrRemoveTabContentView(it)
        }
        viewData.startTabManageActivity.observe(this) {
            startTabManageActivity()
        }
    }

    private fun startTabManageActivity() {
        startActivity(Intent(this, TabManageActivity::class.java))
    }

    private fun addOrRemoveTabContentView(content: View?) {
        binding.webViewContainer.removeAllViews()
        content ?: return
        content.removeFromParent()
        binding.webViewContainer.addView(content, newMatchParentLayoutParam())
    }

    override fun getImplLayoutResId(): Int {
        return R.layout.activity_browser
    }

    override fun onBackPressed() {
        if (viewData.shouldInterceptBackEvent()) return
        super.onBackPressed()
    }
}