package game.riddles.server.util

import android.app.Application
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.tencent.mmkv.MMKV

object ReferrerManager {
    fun readReferrer(application: Application){
        if(getLocalReferrer().isEmpty()){
            val referrerClient = InstallReferrerClient.newBuilder(application).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    try {
                        when (responseCode) {
                            InstallReferrerClient.InstallReferrerResponse.OK -> {
                                val installReferrer = referrerClient.installReferrer.installReferrer

                                MMKV.defaultMMKV().encode("go_referrer",installReferrer)
                            }
                            else->{

                            }
                        }
                    } catch (e: Exception) { }
                    try {
                        referrerClient.endConnection()
                    } catch (e: Exception) {
                    }
                }
                override fun onInstallReferrerServiceDisconnected() {
                }
            })
        }
    }

    private fun getLocalReferrer()=MMKV.defaultMMKV().decodeString("go_referrer", "")?:""


    fun isBuyUser():Boolean{
        val referrer = getLocalReferrer()
        return referrer.contains("fb4a")||
                referrer.contains("gclid")||
                referrer.contains("not%20set")||
                referrer.contains("youtubeads")||
                referrer.contains("%7B%22")
    }
}