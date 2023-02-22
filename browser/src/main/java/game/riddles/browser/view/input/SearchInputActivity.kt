package game.riddles.browser.view.input

import android.content.Intent
import androidx.activity.viewModels
import com.blankj.utilcode.util.KeyboardUtils
import game.riddles.browser.R
import game.riddles.browser.cons.IntentKeys
import game.riddles.browser.databinding.ActivitySearchInputBinding
import game.riddles.browser.view.web.BrowserActivity
import game.riddles.core.top.BaseActivity
import game.riddles.core.utils.adaptStatusBar
import game.riddles.core.utils.setEditorActionCallback

class SearchInputActivity : BaseActivity<ActivitySearchInputBinding, SearchInputData>() {
    override val viewData: SearchInputData by viewModels()
    override fun onInitView() {
        binding.topLayout.adaptStatusBar()
        viewData.startWeb.observe(this) {
            startWeb(it)
        }
        binding.etSearchContent.setEditorActionCallback(
            onSearchAction = { viewData.onCompleteInput() }
        )
        binding.root.postDelayed({
            KeyboardUtils.showSoftInput(binding.etSearchContent)
        }, 200L)
    }

    override fun onDestroy() {
        super.onDestroy()
        KeyboardUtils.unregisterSoftInputChangedListener(window)
    }

    private fun startWeb(searchContent: String) {
        startActivity(Intent(this, BrowserActivity::class.java).apply {
            putExtra(IntentKeys.SEARCH_KEYWORDS, searchContent)
        })
        finish()
    }

    override fun getImplLayoutResId(): Int {
        return R.layout.activity_search_input
    }
}