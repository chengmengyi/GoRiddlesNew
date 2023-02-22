package game.riddles.server.bean

class AdResultBean(
    val time:Long=0L,
    val ad:Any?=null
) {
    fun expired()=(System.currentTimeMillis() - time) >=3600000L
}