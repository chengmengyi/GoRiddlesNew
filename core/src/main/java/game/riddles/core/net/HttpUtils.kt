package game.riddles.core.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HttpUtils {

    @JvmStatic
    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://photo.goriddles.net/")
            .client(
                OkHttpClient
                    .Builder()
                    .addInterceptor(CommonResponseIntercept())
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}