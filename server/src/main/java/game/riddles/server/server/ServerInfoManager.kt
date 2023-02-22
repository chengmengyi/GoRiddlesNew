package game.riddles.server.server

import game.riddles.server.bean.ServerBean

object ServerInfoManager {
    var cityList= arrayListOf<String>()
    val localServerList= arrayListOf<ServerBean>()
    val onlineServerList= arrayListOf<ServerBean>()

    fun getAllServerList()= onlineServerList.ifEmpty { localServerList }

    fun getFastServer(): ServerBean {
        val serverList = getAllServerList()
        if (!cityList.isNullOrEmpty()){
            val filter = serverList.filter { cityList.contains(it.goRi_city) }
            if (!filter.isNullOrEmpty()){
                return filter.random()
            }
        }
        return serverList.random()
    }
}