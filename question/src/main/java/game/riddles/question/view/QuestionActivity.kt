package game.riddles.question.view

import androidx.activity.viewModels
import com.blankj.utilcode.util.ActivityUtils
import game.riddles.ad.apis.SceneAdsApi
import game.riddles.core.top.BaseActivity
import game.riddles.core.utils.adaptStatusBar
import game.riddles.question.R
import game.riddles.question.databinding.ActivityQuestionBinding
import game.riddles.question.entity.MQuestion

class QuestionActivity : BaseActivity<ActivityQuestionBinding, QuestionData>() {
    override val viewData: QuestionData by viewModels()

    override fun onInitView() {
        binding.topLayout.adaptStatusBar()
        binding.popupViewHelp.adaptStatusBar()
        viewData.viewTips.observe(this) {
            showTipsDialog(it)
        }
        viewData.showGetCoinsDialog.observe(this) {
            showGetCoinsDialog()
        }
        viewData.showGetCoinsResultDialog.observe(this) {
            showGetCoinsResultDialog(it)
        }
        viewData.showTimeSceneAd.observe(this) {
            showTimeSceneAd(it)
        }
        viewData.showQuestionEndDialog.observe(this) {
            showQuestionEndDialog()
        }
        viewData
    }

    private fun showQuestionEndDialog() {
        QuestionEndDialog.show(this) {
            viewData.generateNewQuestions()
        }
    }

    private fun showTimeSceneAd(result: Any) {
        SceneAdsApi.showTime(this, result, viewData.timeSceneAdShowCallback)
    }

    private fun showGetCoinsResultDialog(coin: Int) {
        GetCoinsResultDialog.show(this, coin)
    }

    private fun showGetCoinsDialog() {
        GetCoinsDialog.show(this) { showRewardAd() }
    }

    private fun showTipsDialog(question: MQuestion) {
        ViewTipsDialog.show(
            context = this,
            question = question,
            onShowRewardAd = {
                showRewardAd()
            }
        )
    }

    private fun showRewardAd() {
        viewData.enableViews.value = false
        SceneAdsApi.loadReward(viewData.getApplication())
        val result = SceneAdsApi.resultReward()
        if (result == null) {
            viewData.enableViews.value = true
            return
        }
        SceneAdsApi.showReward(this, result, viewData.rewardSceneAdShowCallback)
    }

    override fun onBackPressed() {
        ActivityUtils.startHomeActivity()
    }

    override fun getImplLayoutResId(): Int {
        return R.layout.activity_question
    }
}