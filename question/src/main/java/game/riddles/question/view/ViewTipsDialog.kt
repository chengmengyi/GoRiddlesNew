package game.riddles.question.view

import android.annotation.SuppressLint
import android.content.Context
import com.lxj.xpopup.XPopup
import game.riddles.core.top.BaseCenterDialog
import game.riddles.question.R
import game.riddles.question.databinding.DialogViewTipsBinding
import game.riddles.question.entity.MQuestion

@SuppressLint("ViewConstructor")
internal class ViewTipsDialog private constructor(
    context: Context,
    private val question: MQuestion,
    private val onShowRewardAd: () -> Unit
) : BaseCenterDialog<DialogViewTipsBinding, ViewTipsData>(context) {
    companion object {
        fun show(
            context: Context,
            question: MQuestion,
            onShowRewardAd: () -> Unit
        ) {
            XPopup.Builder(context)
                .isDestroyOnDismiss(true)
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(ViewTipsDialog(context, question, onShowRewardAd))
                .show()
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_view_tips
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate() {
        super.onCreate()
        viewData.closeViewTipDialog.observe(this) {
            dismiss()
        }
        viewData.showGetCoinsDialog.observe(this) {
            dismiss()
            GetCoinsDialog.show(context, onShowRewardAd)
        }
    }

    override fun newViewData(): ViewTipsData {
        return ViewTipsData(question)
    }
}