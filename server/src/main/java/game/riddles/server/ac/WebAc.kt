package game.riddles.server.ac

import game.riddles.server.R
import game.riddles.server.base.BaseAc
import game.riddles.server.conf.Local
import kotlinx.android.synthetic.main.activity_web.*

class WebAc: BaseAc(R.layout.activity_web) {
    override fun initView() {
        immersionBar.statusBarView(top_view).init()
        iv_back.setOnClickListener { finish() }
        web_view.apply {
            settings.javaScriptEnabled=true
            loadUrl(Local.WEB)
        }
    }
}