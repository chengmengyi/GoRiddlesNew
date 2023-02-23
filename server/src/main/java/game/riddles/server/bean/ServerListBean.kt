package game.riddles.server.bean

class ServerListBean(
    val hauls: String="",  //国家码
    val trails: String="Faster server", //国家名字
    val grinder: Int=0,  //城市id
    val commission: String="", //城市名字
    val serverInfo:ServerBean?=null
){
    fun isFast()=trails=="Faster server"
}

