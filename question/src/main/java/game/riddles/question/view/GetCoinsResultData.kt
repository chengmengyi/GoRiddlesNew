package game.riddles.question.view

import android.view.View
import androidx.lifecycle.MutableLiveData
import game.riddles.core.top.BaseViewDataNonComp

class GetCoinsResultData : BaseViewDataNonComp() {
    val closeGetCoinsResultDialog = MutableLiveData<Boolean>()
    val rewardedCoin = MutableLiveData(0)

    fun setRewardedCoin(coin: Int) {
        rewardedCoin.value = coin
    }

    fun onCloseGetCoinsResultDialog(view: View) {
        closeGetCoinsResultDialog.value = true
    }
}