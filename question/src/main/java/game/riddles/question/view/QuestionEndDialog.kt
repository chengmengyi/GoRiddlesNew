package game.riddles.question.view

import android.annotation.SuppressLint
import android.content.Context
import com.lxj.xpopup.XPopup
import game.riddles.core.top.BaseCenterDialog
import game.riddles.question.R
import game.riddles.question.databinding.DialogQuestionEndBinding

@SuppressLint("ViewConstructor")
class QuestionEndDialog private constructor(
    context: Context,
    private val onGenerateNewQuestions: () -> Unit
) : BaseCenterDialog<DialogQuestionEndBinding, QuestionEndData>(
    context
) {

    companion object {
        fun show(context: Context, onGenerateNewQuestions: () -> Unit) {
            XPopup.Builder(context)
                .isDestroyOnDismiss(true)
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(QuestionEndDialog(context, onGenerateNewQuestions))
                .show()
        }
    }

    override fun onCreate() {
        super.onCreate()
        viewData.closeQuestionEndDialog.observe(this) {
            dismiss()
        }
        viewData.generateNewQuestions.observe(this) {
            dismiss()
            onGenerateNewQuestions()
        }
    }

    override fun newViewData(): QuestionEndData {
        return QuestionEndData()
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_question_end
    }
}