package game.riddles.ad.entiti

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@Keep
@JsonClass(generateAdapter = true)
data class MAdData(
    @Json(name = "from")
    val platform: String,
    @Json(name = "data_id")
    val dataId: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "index")
    val sort: Int
) : Serializable

@Keep
@JsonClass(generateAdapter = true)
data class MAdConfig(
    @Json(name = "enterid_open")
    val open: List<MAdData> = emptyList(),
    @Json(name = "enterid_reward")
    val reward: List<MAdData> = emptyList(),
    @Json(name = "enterid_time")
    val time: List<MAdData> = emptyList()
) : Serializable
