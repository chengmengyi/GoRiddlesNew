package game.riddles.question.view

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import game.riddles.ad.apis.SceneAdsApi
import game.riddles.core.top.BaseViewData
import game.riddles.core.top.ILayoutLifecycleObserver
import game.riddles.question.R
import game.riddles.question.entity.MQuestion
import game.riddles.question.factory.newQuestionsRepository
import game.riddles.question.factory.newUsrManager
import game.riddles.question.usr.OnUsrAttrChanged
import game.riddles.question.utils.getLevelIconRes
import game.riddles.question.utils.setAnswered
import game.riddles.server.ServerApi
import kotlinx.coroutines.*

class QuestionData(application: Application) : BaseViewData(application), OnUsrAttrChanged,
    ILayoutLifecycleObserver {
    private class OptionRecord(val questionDataIndex: Int, val optionIndex: Int)

    val usrLevelExpText = MutableLiveData("0")
    val usrCoinText = MutableLiveData("0")
    val usrLevelIcon = MutableLiveData(R.mipmap.level_4)
    val questionSortNum = MutableLiveData(1)
    val curQuestion = MutableLiveData<MQuestion?>()
    val rightOptionIndex = MutableLiveData(-1)
    val usrOptionIndex = MutableLiveData(-2)
    val viewTips = MutableLiveData<MQuestion>()
    val guideViewTips = MutableLiveData(false)
    val showGetCoinsDialog = MutableLiveData<Boolean>()
    val showGetCoinsResultDialog = MutableLiveData<Int>()
    internal val showQuestionEndDialog = MutableLiveData<Boolean>()
    internal val showTimeSceneAd = MutableLiveData<Any>()
    val enableViews = MutableLiveData(true)
    val rewardSceneAdShowCallback = object : SceneAdsApi.OnSceneAdShowCallback {
        override fun onAdReward() {
            SceneAdsApi.loadReward(getApplication())
            mUsrManager.increaseCoin(40)
            showGetCoinsResultDialog.value = 40
            enableViews.value = true
        }

        override fun onAdShowCompleted() {
            SceneAdsApi.loadReward(getApplication())
            enableViews.value = true
        }
    }
    val timeSceneAdShowCallback = object : SceneAdsApi.OnSceneAdShowCallback {
        override fun onAdShowCompleted() {
            onNextQuestion()
            startStayDurationJob()
            SceneAdsApi.loadTime(getApplication())
            enableViews.value = true
        }
    }
    private val mQuestions: MutableList<MQuestion> = mutableListOf()
    private var mQuestionIndex = -1
        set(value) {
            field = value
            questionSortNum.value = value + 1
        }
    private var mUsrRightedCount = 0
    private val mUsrOptionedIndexRecord = mutableSetOf<OptionRecord>()
    private val mQuestionsRepository = newQuestionsRepository(viewModelScope)
    private val mUsrManager = newUsrManager()
    private var mStartTipsGuideJob: Job? = null
    private var mStayDurationJob: Job? = null
    private var mCanShowNextAd = false

    init {
        initQuestionLib()
        usrLevelExpText.value = mUsrManager.getLevelExp().toString()
        usrCoinText.value = mUsrManager.getCoinCount().toString()
        usrLevelIcon.value = getLevelIconRes(mUsrManager.getLevel())
        mUsrManager.addUsrAttrChangeListener(this)
    }

    private fun resetUi() {
        mQuestions.clear()
        mQuestionIndex = -1
        mUsrOptionedIndexRecord.clear()
        mUsrRightedCount = 0
        cancelTipsGuideJob()
        cancelStayDurationJob()
        curQuestion.value = null
        rightOptionIndex.value = -1
        usrOptionIndex.value = -2
        guideViewTips.value = false
    }

    private fun initQuestionLib() {
        viewModelScope.launch(Dispatchers.Main) {
            val questions = withContext(Dispatchers.IO) {
                mQuestionsRepository.listQuestionsByUsrLevel()
            }
            mQuestions.addAll(questions)
            if (mQuestions.isNotEmpty()) {
                changeQuestion(mQuestionIndex + 1)
                startStayDurationJob()
            }
        }
    }

    private fun startStayDurationJob() {
        cancelStayDurationJob()
        mStayDurationJob = viewModelScope.launch(Dispatchers.Main) {
            delay(1000L * 60L * 3L)
            mCanShowNextAd = true
        }
    }

    private fun cancelStayDurationJob() {
        mCanShowNextAd = false
        mStayDurationJob?.cancel()
        mStayDurationJob = null
    }

    private fun changeQuestion(questionDataIndex: Int) {
        if (mQuestions.lastIndex < questionDataIndex) return
        cancelTipsGuideJob()
        rightOptionIndex.value = -1
        usrOptionIndex.value = -2
        mQuestionIndex = questionDataIndex
        val question = mQuestions[questionDataIndex]
        curQuestion.value = question
        val usrOptionedRecord = usrOptioned(questionDataIndex)
        question.options.forEachIndexed { index, mOption ->
            if (mOption.isAnswer) {
                rightOptionIndex.value = index
                usrOptionIndex.value = usrOptionedRecord?.optionIndex ?: -1
                return@forEachIndexed
            }
        }
        if (usrOptionedRecord == null) {
            startTipsGuideJob()
        }
    }

    private fun usrOptioned(questionDataIndex: Int): OptionRecord? {
        mUsrOptionedIndexRecord.forEach {
            if (it.questionDataIndex == questionDataIndex) return it
        }
        return null
    }

    fun onOption(index: Int) {
        cancelTipsGuideJob()
        usrOptionIndex.value = index
        mUsrOptionedIndexRecord.add(OptionRecord(mQuestionIndex, index))
        if (rightOptionIndex.value!! == index) {
            mQuestions.getOrNull(mQuestionIndex)?.setAnswered()
            mUsrRightedCount++
            val increase = if (mUsrManager.getLevel() in 1..5) 5 else 10
            mUsrManager.increaseCoin(increase)
            mUsrManager.increaseExperience(increase)
        }
    }

    fun onNextQuestion() {
        SceneAdsApi.loadTime(getApplication())
        if (mCanShowNextAd) {
            val timeSceneResult = SceneAdsApi.resultTime()
            if (timeSceneResult != null) {
                enableViews.value = false
                showTimeSceneAd.value = timeSceneResult
                return
            }
        }
        val next = mQuestionIndex + 1
        if (next > mQuestions.lastIndex) {
            if (mUsrRightedCount < mQuestions.size) {
                showQuestionEndDialog.value = true
            } else {
                initQuestionLib()
            }
            return
        }
        changeQuestion(next)
    }

    fun generateNewQuestions() {
        resetUi()
        initQuestionLib()
    }

    fun onPreQuestion() {
        val pre = mQuestionIndex - 1
        if (pre < 0) return
        changeQuestion(pre)
    }

    fun onSearchQuestion(view: View) {
        runCatching {
            view.context.startActivity(
                Intent(
                    view.context,
                    Class.forName("game.riddles.browser.view.web.BrowserActivity")
                )
            )
        }
    }

    fun onDismissGuideTips() {
        guideViewTips.value = false
    }

    private fun startTipsGuideJob() {
        mStartTipsGuideJob?.cancel()
        mStartTipsGuideJob = viewModelScope.launch(Dispatchers.Main) {
            delay(10000L)
            cancelTipsGuideJob()
            guideViewTips.value = true
        }
    }

    private fun cancelTipsGuideJob() {
        onDismissGuideTips()
        mStartTipsGuideJob?.cancel()
        mStartTipsGuideJob = null
    }

    fun onViewTips(view: View) {
        cancelTipsGuideJob()
        mQuestions.getOrNull(mQuestionIndex)?.let {
            viewTips.value = it
        }
    }

    fun onShowGetCoinsDialog(view: View) {
        showGetCoinsDialog.value = true
    }

    fun toServerAc(view: View){
        ServerApi.startServerHomeActivity(view.context,showBottom = true)
    }

    override fun onCoinCountChanged(coinCount: Int) {
        usrCoinText.value = coinCount.toString()
    }

    override fun onLevelChanged(level: Int) {
        usrLevelIcon.value = getLevelIconRes(level)
    }

    override fun onLevelExpChanged(exp: Int) {
        usrLevelExpText.value = exp.toString()
    }

    override fun onCleared() {
        super.onCleared()
        mUsrOptionedIndexRecord.clear()
        mUsrManager.removeUsrAttrChangeListener(this)
    }

    override fun onLayoutCreate(savedInstanceState: Bundle?) {
        SceneAdsApi.loadReward(getApplication())
        SceneAdsApi.loadTime(getApplication())
    }
}