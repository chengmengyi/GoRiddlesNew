package game.riddles.server.tba

import android.app.Application
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import game.riddles.server.util.logGo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

object TbaManager {
    var isCloak=false
    private var ip=""
    private const val cloakUrl="https://majesty.goriddles.net/utopian/sandal/sapphire/indorse"

    private fun requestIp(callback:()->Unit){
        if (ip.isEmpty()){
            OkGo.get<String>("https://api.myip.com/").execute(object : StringCallback(){
                override fun onSuccess(response: Response<String>?) {
                    //    {"ip":"23.27.206.181","country":"United States","cc":"US"}
                    try {
                        ip= JSONObject(response?.body().toString()).optString("ip")
                        callback.invoke()
                    }catch (e:Exception){

                    }
                }
            })
        }else{
            callback.invoke()
        }
    }


    fun getCloak(application: Application){
        requestIp {
            GlobalScope.launch {
                val path="${cloakUrl}?attend=${getDistinctId(application)}&madhya=${ip}&palmate=${getClientTs()}&tassel=${getDeviceModel()}&shasta=${getBundleId(application)}&charcoal=${getOsVersion()}&hardcopy=${getGaid(application)}&tubule=${getAndroidId(application)}&suppress=${getOs()}&textron=${getAppVersion(application)}&dortmund=${getBrand()}"
//                logGo(path)
                OkGo.get<String>(path).execute(object : StringCallback(){
                    override fun onSuccess(response: Response<String>?) {
                        try {
//                            logGo("=getCloak===${response?.body().toString()}===")
                            isCloak=response?.body().toString()=="oar"
                        }catch (e:Exception){

                        }
                    }

                    override fun onError(response: Response<String>?) {
                        super.onError(response)

                    }
                })
            }
        }
    }
}