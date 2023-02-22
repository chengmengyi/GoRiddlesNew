package game.riddles.question.view

import android.view.View
import androidx.lifecycle.MutableLiveData
import game.riddles.core.top.BaseViewDataNonComp
import game.riddles.question.entity.MQuestion
import game.riddles.question.factory.newUsrManager

class ViewTipsData(private val mQuestion: MQuestion) : BaseViewDataNonComp() {
    val closeViewTipDialog = MutableLiveData<Boolean>()
    val freeTipsOpportunity = MutableLiveData(0)
    val question = MutableLiveData(mQuestion)
    val usrTipsOptionedIndex = MutableLiveData(-1)
    val answer = MutableLiveData<String>()
    val showGetCoinsDialog = MutableLiveData<Boolean>()

    private val mUsrManager = newUsrManager()
    private var mFreeTipsOpportunity = 0
        set(value) {
            field = value
            freeTipsOpportunity.value = value
        }
    private var mAnswer = ""
        set(value) {
            field = value
            answer.value = value
        }

    init {
        mFreeTipsOpportunity = mUsrManager.getFreeTipsOpportunity()
        mAnswer = mQuestion.options.first { it.isAnswer }.text
    }

    fun onCloseViewTipDialog(view: View) {
        closeViewTipDialog.value = true
    }

    fun onViewTips(tipsOptionedIndex: Int) {
        if (mFreeTipsOpportunity > 0) {
            mFreeTipsOpportunity--
            mUsrManager.deductFreeTipsOpportunity()
            usrTipsOptionedIndex.value = tipsOptionedIndex
            return
        }
        val deductCoin = when (tipsOptionedIndex) {
            0 -> 10
            1 -> 15
            else -> throw IllegalArgumentException("tipsOptionedIndex must be 0 or 1")
        }
        if (mUsrManager.getCoinCount() < deductCoin) {
            showGetCoinsDialog.value = true
            return
        }
        usrTipsOptionedIndex.value = tipsOptionedIndex
        mUsrManager.deductCoin(deductCoin)
    }
}