package game.riddles.core.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HttpUtils {

    @JvmStatic
    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://rejoiceyah-c9239ab53e124c97.elb.us-east-1.amazonaws.com/")
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