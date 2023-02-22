package game.riddles.core.net

import okhttp3.Interceptor
import okhttp3.Response

class CommonResponseIntercept : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response: Response
        try {
            response = chain.proceed(request)
            val code = response.code
//            if (code != 200) throw IllegalStateException("Http Code Exception: code = ${code}, message = ${response.message}")
        } catch (e: Exception) {
            throw e
        }
        return response
    }
}