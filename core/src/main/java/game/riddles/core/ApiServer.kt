package game.riddles.core

import com.blankj.utilcode.util.DeviceUtils
import game.riddles.core.utils.timeZone
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*

interface ApiServer {

    interface IEvent {

        @POST("/rejoice/yah/helmut/bluebook")
        suspend fun upload(
            @Header("knives") timeZone: Int = timeZone(),
            @Header("biota") storageSize: Long = 0L,
            @Query("tassel") deviceModel: String = DeviceUtils.getModel(),
            @Query("hardcopy") googleAdsId: String,
            @Query("cactus") randomUUID: String = UUID.randomUUID().toString(),
            @Body body: RequestBody
        ): Any?
    }
}
