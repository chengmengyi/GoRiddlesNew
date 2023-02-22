package game.riddles.browser.view.tab

import androidx.activity.viewModels
import game.riddles.browser.R
import game.riddles.browser.databinding.ActivityTabManageBinding
import game.riddles.core.adapter.grid
import game.riddles.core.adapter.setBaseAdapter
import game.riddles.core.top.BaseActivity
import game.riddles.core.utils.adaptStatusBar
import game.riddles.core.utils.applySingleDenouncingClick

class TabManageActivity : BaseActivity<ActivityTabManageBinding, TabManageData>() {
    override val viewData: TabManageData by viewModels()

    override fun onInitView() {
        val that = this
        binding.topLayout.adaptStatusBar()
        binding.btnBack.applySingleDenouncingClick { onBackPressed() }
        binding.btnAddNewTab.applySingleDenouncingClick { viewData.addNewTab() }
        binding.rvTabManage
            .grid(2)
            .setBaseAdapter()
            .bingLayout(R.layout.item_tab_manage)
            .bindLiveData(this, viewData.tabList)
        viewData.finishTabManagerActivity.observe(this) {
            that.finish()
        }
    }

    override fun getImplLayoutResId(): Int {
        return R.layout.activity_tab_manage
    }
}