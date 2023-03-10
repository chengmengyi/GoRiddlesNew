package game.riddles.server.server

import android.util.Log
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import game.riddles.server.LoadingDialog
import game.riddles.server.bean.ConfigServerBean
import game.riddles.server.bean.ServerBean
import game.riddles.server.bean.ServerInfoBean
import game.riddles.server.bean.ServerListBean
import game.riddles.server.conf.Local
import game.riddles.server.myApp
import game.riddles.server.tba.getAndroidId
import game.riddles.server.util.logGo
import org.json.JSONObject
import java.util.*

object ServerInfoManager {
    var loadOnlineSuccess=false
    private const val url="https://fastseverconfig.com/"

    var cityList= arrayListOf<String>()
    val localServerList= arrayListOf<ServerBean>()
    val onlineServerList= arrayListOf<ServerBean>()

    val configServerList= arrayListOf<ServerListBean>()

    fun getConfigAllServerList(){
        parseLocalServerJson()

        OkGo.get<String>("${url}api/server/list/")
            .headers("ctr", Locale.getDefault().country)
//            .headers("pkg", myApp.packageName)
            .headers("pkg", "com.goriddles.ingenuity.answer")
            .headers("dev", getAndroidId(myApp))
            .execute(object : StringCallback(){
                override fun onSuccess(response: Response<String>?) {
                    val base64Body = base64Body(response?.body()?.toString() ?: "")
                    logGo("==onSuccess===${base64Body}=")
                    parseServerJson(base64Body)
                }

                override fun onError(response: Response<String>?) {
                    super.onError(response)
                    loadOnlineSuccess=false
                    Log.e("qwer","=onError===${response?.body()?.toString()}=")
                }
            })
    }

    private fun parseLocalServerJson(){
        try {
            configServerList.clear()
            val list = Gson().fromJson<ArrayList<ServerBean>>(Local.SERVER_LIST, object : TypeToken<ArrayList<ServerBean>>() {}.type)
            for (serverBean in list) {
                serverBean.writeServerId()
                configServerList.add(
                    ServerListBean(
                        trails = serverBean.map?:"",
                        commission = serverBean.volts?:"",
                        serverInfo = serverBean
                    )
                )
            }
        }catch (e:Exception){

        }
    }

    private fun parseServerJson(string: String){
        try {
            val configServerBean = Gson().fromJson<ConfigServerBean>(string, ConfigServerBean::class.java)
            if(configServerBean.c==1290){
                val noun = configServerBean.d?.noun
                if (noun?.isNotEmpty() == true){
                    configServerList.clear()
                    loadOnlineSuccess=true
                    for (n in noun) {
                        if(null!=n?.anchors){
                            for (anchor in n.anchors) {
                                configServerList.add(
                                    ServerListBean(
                                        hauls = n.haculs?:"",
                                        trails = n.trails?:"",
                                        grinder = anchor?.grinder?:0,
                                        commission = anchor?.commission?:""
                                    )
                                )
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {

        }
    }

    private fun base64Body(string: String):String=String(android.util.Base64.decode(StringBuffer(string).reverse().toString(),android.util.Base64.DEFAULT))


    fun getServerInfo(fragmentManager: FragmentManager,cityId:Int?,callback:(bean:ServerBean?)->Unit){
        var path="${url}api/server/sandm/?departure=ss"
        if(null!=cityId&&cityId>0){
            path+="&staff=${cityId}"
        }
        val loadingDialog = LoadingDialog()
        loadingDialog.show(fragmentManager,"LoadingDialog")
        OkGo.get<String>(path)
            .headers("ctr", Locale.getDefault().country)
//            .headers("pkg", myApp.packageName)
            .headers("pkg", "com.goriddles.ingenuity.answer")
            .headers("dev", getAndroidId(myApp))
            .execute(object : StringCallback(){
                override fun onSuccess(response: Response<String>?) {
                    loadingDialog.dismiss()
                    val base64Body = base64Body(response?.body()?.toString() ?: "")
                    logGo("==onSuccess===${base64Body}=")
                    parseServerInfoJson(base64Body,callback)
                }

                override fun onError(response: Response<String>?) {
                    super.onError(response)
                    loadingDialog.dismiss()
                    Log.e("qwer","=onError===${response?.body()?.toString()}=")
                    callback.invoke(null)
                }
            })
    }

    private fun parseServerInfoJson(json:String,callback:(bean:ServerBean?)->Unit){
        try {
            val serverInfoBean = Gson().fromJson<ServerInfoBean>(json, ServerInfoBean::class.java)
            if(serverInfoBean.c==1290&& null!=serverInfoBean.d && serverInfoBean.d.isNotEmpty()){
                val random = serverInfoBean.d.random()
                random?.writeServerId()
                callback.invoke(random)
            }else{
                callback.invoke(null)
            }
        } catch (e: Exception) {
            callback.invoke(null)
        }
    }
}