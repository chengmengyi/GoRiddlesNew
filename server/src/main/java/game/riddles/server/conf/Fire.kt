package game.riddles.server.conf


import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.tencent.mmkv.MMKV
import game.riddles.server.bean.ServerBean
import game.riddles.server.server.ServerInfoManager
import game.riddles.server.util.AdShowed
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

object Fire {
    var irUser=false

    fun readFire(){
        readLocalServerJson()
        checkIRUser()
//        val remoteConfig = Firebase.remoteConfig
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            if (it.isSuccessful){
//                parseServerJson(remoteConfig.getString("flash_ser"))
//                parseCity(remoteConfig.getString("flash_smart"))
//                saveAdJson(remoteConfig.getString("riddles_ad"))
//            }
//        }
    }

    private fun parseServerJson(string: String){
        try {
            ServerInfoManager.onlineServerList.clear()
            val jsonArray = JSONArray(string)
            for (index in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(index)
                ServerInfoManager.onlineServerList.add(
                    ServerBean(
                        goRi_pwd = jsonObject.optString("goRi_pwd"),
                        goRi_account=jsonObject.optString("goRi_account"),
                        goRi_port = jsonObject.optInt("goRi_port"),
                        goRi_country=jsonObject.optString("goRi_country"),
                        goRi_city=jsonObject.optString("goRi_city"),
                        goRi_ip=jsonObject.optString("goRi_ip")
                    )
                )
            }
            writeServer(ServerInfoManager.onlineServerList)
        }catch (e:Exception){

        }
    }

    private fun parseCity(string: String){
        try {
            ServerInfoManager.cityList.clear()
            val jsonArray = JSONArray(string)
            for (index in 0 until jsonArray.length()){
                ServerInfoManager.cityList.add(jsonArray.optString(index))
            }
        }catch (e:Exception){

        }
    }

    private fun saveAdJson(string:String){
        try {
            MMKV.defaultMMKV().encode("riddles_ad",string)
            val jsonObject = JSONObject(string)
            AdShowed.setMax(jsonObject.optInt("max_click"),jsonObject.optInt("max_show"))
        }catch (e:Exception){
            MMKV.defaultMMKV().encode("riddles_ad","")
        }

    }

    fun readAdJson():String{
        val s = MMKV.defaultMMKV().decodeString("riddles_ad") ?: ""
        if (s.isEmpty()) {
            return Local.AD_LIST
        }
        return s
    }

    private fun readLocalServerJson(){
        try {
            ServerInfoManager.localServerList.clear()
            val jsonArray = JSONArray(Local.SERVER_LIST)
            for (index in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(index)
                ServerInfoManager.localServerList.add(
                    ServerBean(
                        goRi_pwd = jsonObject.optString("goRi_pwd"),
                        goRi_account=jsonObject.optString("goRi_account"),
                        goRi_port = jsonObject.optInt("goRi_port"),
                        goRi_country=jsonObject.optString("goRi_country"),
                        goRi_city=jsonObject.optString("goRi_city"),
                        goRi_ip=jsonObject.optString("goRi_ip")
                    )
                )
            }
            writeServer(ServerInfoManager.localServerList)
        }catch (e:Exception){

        }
    }

    private fun writeServer(list:ArrayList<ServerBean>){
        list.forEach { it.writeServerId() }
    }

    private fun checkIRUser(){
        val country = Locale.getDefault().country
        if(country=="IR"){
            irUser =true
        }else{
            OkGo.get<String>("https://api.myip.com/")
                .execute(object : StringCallback(){
                    override fun onSuccess(response: Response<String>?) {
//                        ipJson="""{"ip":"89.187.185.11","country":"United States","cc":"IR"}"""
                        try {
                            irUser = JSONObject(response?.body()?.toString()).optString("cc")=="IR"
                        }catch (e:Exception){

                        }
                    }
                })
        }
    }
}