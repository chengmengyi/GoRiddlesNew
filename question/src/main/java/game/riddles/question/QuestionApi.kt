package game.riddles.question

import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import game.riddles.question.view.QuestionActivity

object QuestionApi {

    @JvmStatic
    fun startQuestionActivity(context: Context) {
        if (ActivityUtils.isActivityExistsInStack(QuestionActivity::class.java)) {
            return
        }
        context.startActivity(Intent(context, QuestionActivity::class.java))
    }
}