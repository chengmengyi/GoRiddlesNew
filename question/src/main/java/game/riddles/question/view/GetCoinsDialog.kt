package game.riddles.question.view

import android.annotation.SuppressLint
import android.content.Context
import com.lxj.xpopup.XPopup
import game.riddles.core.top.BaseCenterDialog
import game.riddles.question.R
import game.riddles.question.databinding.DialogGetCoinsBinding

@SuppressLint("ViewConstructor")
class GetCoinsDialog private constructor(context: Context, private val onShowRewardAd: () -> Unit) :
    BaseCenterDialog<DialogGetCoinsBinding, GetCoinsData>(
        context
    ) {

    companion object {
        fun show(context: Context, onShowRewardAd: () -> Unit) {
            XPopup.Builder(context)
                .isDestroyOnDismiss(true)
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(GetCoinsDialog(context, onShowRewardAd))
                .show()
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_get_coins
    }

    override fun newViewData(): GetCoinsData {
        return GetCoinsData()
    }

    override fun onCreate() {
        super.onCreate()
        viewData.closeGetCoinsDialog.observe(this) {
            dismiss()
        }
        viewData.closeGetCoinsDialogAndShowRewardAd.observe(this) {
            dismiss()
            onShowRewardAd()
        }
    }
}