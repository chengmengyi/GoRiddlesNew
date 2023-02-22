package game.riddles.question.view

import android.annotation.SuppressLint
import android.content.Context
import com.lxj.xpopup.XPopup
import game.riddles.core.top.BaseCenterDialog
import game.riddles.question.R
import game.riddles.question.databinding.DialogGetCoinsResultBinding

@SuppressLint("ViewConstructor")
class GetCoinsResultDialog private constructor(context: Context, private val coin: Int) :
    BaseCenterDialog<DialogGetCoinsResultBinding, GetCoinsResultData>(
        context
    ) {

    companion object {
        fun show(context: Context, coin: Int) {
            XPopup.Builder(context)
                .isDestroyOnDismiss(true)
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(GetCoinsResultDialog(context, coin))
                .show()
        }
    }

    override fun onCreate() {
        super.onCreate()
        viewData.closeGetCoinsResultDialog.observe(this) {
            dismiss()
        }
        viewData.setRewardedCoin(coin)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_get_coins_result
    }

    override fun newViewData(): GetCoinsResultData {
        return GetCoinsResultData()
    }
}