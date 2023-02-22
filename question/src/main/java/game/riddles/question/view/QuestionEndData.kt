package game.riddles.question.view

import android.view.View
import androidx.lifecycle.MutableLiveData
import game.riddles.core.top.BaseViewDataNonComp

class QuestionEndData : BaseViewDataNonComp() {
    val closeQuestionEndDialog = MutableLiveData<Boolean>()
    val generateNewQuestions = MutableLiveData<Boolean>()

    fun onCloseQuestionEndDialog(view: View) {
        closeQuestionEndDialog.value = true
    }

    fun onGenerateNewQuestions(view: View) {
        generateNewQuestions.value = true
    }
}