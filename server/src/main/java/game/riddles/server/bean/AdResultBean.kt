package game.riddles.server.bean

import androidx.annotation.Keep

@Keep
class AdResultBean(
    val time:Long=0L,
    val ad:Any?=null
) {
    fun expired()=(System.currentTimeMillis() - time) >=3600000L
}