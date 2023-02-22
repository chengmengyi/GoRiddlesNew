package game.riddles.server.admob


import game.riddles.server.bean.AdBean
import game.riddles.server.bean.AdResultBean
import game.riddles.server.conf.Fire
import game.riddles.server.conf.Local
import game.riddles.server.util.AdShowed
import game.riddles.server.util.logGo
import org.json.JSONObject

object LoadAdImpl: BaseLoadAd() {
    var showingFull=false

    private val loading= arrayListOf<String>()
    private val loadResult= hashMapOf<String, AdResultBean>()

    fun load(type:String,retryNum:Int=0){
        return
        if (AdShowed.limit()){
            logGo("limit")
            return
        }
        if (loading.contains(type)){
            logGo("$type loading")
            return
        }
        if(loadResult.containsKey(type)){
            val adResultBean = loadResult[type]
            if (null!=adResultBean?.ad){
                if(adResultBean.expired()){
                    removeAd(type)
                }else{
                    logGo("$type cache")
                    return
                }
            }
        }

        val adList = getAdList(type)
        if (adList.isEmpty()){
            logGo("$type ad list empty")
            return
        }
        loading.add(type)
        loopLoadAd(type,adList.iterator(),retryNum)
    }

    private fun loopLoadAd(type: String, iterator: Iterator<AdBean>, retryNum:Int){
        loadAdByType(type,iterator.next()){
            if (null==it){
                if (iterator.hasNext()){
                    loopLoadAd(type,iterator,retryNum)
                }else{
                    loading.remove(type)
                    if (type== Local.OPEN&&retryNum>0){
                        load(type)
                    }
                }
            }else{
                loading.remove(type)
                loadResult[type]=it
            }
        }
    }

    private fun getAdList(type: String):List<AdBean>{
        val list= arrayListOf<AdBean>()
        try {
            val jsonArray = JSONObject(Fire.readAdJson()).getJSONArray(type)
            for (index in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(index)
                list.add(
                    AdBean(
                        jsonObject.optString("from"),
                        jsonObject.optString("data_id"),
                        jsonObject.optString("type"),
                        jsonObject.optInt("index"),
                    )
                )
            }
        }catch (e:Exception){ }
        return list.filter { it.from == "admob" }.sortedByDescending { it.index }
    }

    fun getAdResult(type: String)= loadResult[type]?.ad

    fun removeAd(type: String){
        loadResult.remove(type)
    }

    fun preLoadAd(){
        load(Local.OPEN, retryNum = 1)
        load(Local.CONNECT)
        load(Local.HOME)
        load(Local.RESULT)
    }
}