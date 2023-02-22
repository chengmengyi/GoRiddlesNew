package game.riddles.question.view

import android.view.View
import androidx.lifecycle.MutableLiveData
import game.riddles.core.top.BaseViewDataNonComp

class GetCoinsData : BaseViewDataNonComp() {
    val closeGetCoinsDialog = MutableLiveData<Boolean>()
    val closeGetCoinsDialogAndShowRewardAd = MutableLiveData<Boolean>()

    fun onCloseGetCoinsDialog(view: View) {
        closeGetCoinsDialog.value = true
    }

    fun onGetCoinByRewardAd(view: View) {
        closeGetCoinsDialogAndShowRewardAd.value = true
    }
}