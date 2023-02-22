package game.riddles.browser.view.input

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import game.riddles.core.top.BaseViewData

class SearchInputData(application: Application) : BaseViewData(application) {
    val searchContent = MutableLiveData("")
    val startWeb = MutableLiveData<String>()

    fun onClearInput(view: View) {
        searchContent.value = ""
    }

    fun onCompleteInput() {
        val searchContent = searchContent.value ?: return
        startWeb.value = searchContent
    }
}