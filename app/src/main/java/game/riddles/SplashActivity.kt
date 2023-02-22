package game.riddles

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import game.riddles.ad.apis.SceneAdsApi
import game.riddles.core.top.BaseActivity
import game.riddles.databinding.ActivitySplashBinding
import game.riddles.question.QuestionApi
import game.riddles.server.ServerApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashData>(),
    SceneAdsApi.OnSceneAdShowCallback {
    override val viewData: SplashData by viewModels()

    override fun onInitView() {
        ServerApi.preLoadAllAd()
        viewData.startQuestion.observe(this) {
            startQuestion(it)
        }
        viewData.showOpenAd.observe(this) {
            showOpenAd(it)
        }
        viewData.startSplashProgress()
    }

    private fun showOpenAd(data: Any) {
        SceneAdsApi.showOpen(this, data, this)
    }

    override fun onAdShowCompleted() {
        viewData.onAdShowCompleted()
    }

    private fun startQuestion(nextLoop: Boolean) {
        if (nextLoop) {
            lifecycleScope.launch(Dispatchers.Main) {
                startQuestion(false)
            }
        } else {
            if (ServerApi.showOldAc()){
                QuestionApi.startQuestionActivity(this)
            }else{
                ServerApi.startServerHomeActivity(this)
            }
            finish()
        }
    }

    override fun getImplLayoutResId(): Int {
        return R.layout.activity_splash
    }

    override fun onBackPressed() {
    }


}