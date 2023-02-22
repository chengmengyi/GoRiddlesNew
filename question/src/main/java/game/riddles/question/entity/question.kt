package game.riddles.question.entity

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@Keep
@JsonClass(generateAdapter = true)
data class MQuestionLib(
    val list: List<MQuestion>
) : Serializable

@Keep
@JsonClass(generateAdapter = true)
data class MQuestion(
    @Json(name = "ask")
    val text: String,
    val options: List<MOption>,
    val explanation: String
) : Serializable

@Keep
@JsonClass(generateAdapter = true)
data class MOption(
    val text: String,
    val isAnswer: Boolean
) : Serializable
