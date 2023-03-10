package game.riddles.server

import android.app.Application
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.github.shadowsocks.Core
import com.tencent.mmkv.MMKV
import game.riddles.server.ac.HomeAc
import game.riddles.server.admob.LoadAdImpl
import game.riddles.server.conf.Fire
import game.riddles.server.conf.ServerOkManager
import game.riddles.server.tba.TbaManager
import game.riddles.server.util.AcRegister
import game.riddles.server.util.AdShowed
import game.riddles.server.util.ReferrerManager

lateinit var myApp: Application

object ServerApi {
    fun initVpnCore(app: Application){
        Core.init(app, HomeAc::class)
    }

    @JvmStatic
    fun init(app: Application) {
        myApp=app
        MMKV.initialize(app)
        AcRegister.register(app)
        ServerOkManager.getServerList()
        Fire.readFire()
    }

    @JvmStatic
    fun startServerHomeActivity(context: Context,showBottom:Boolean=false) {
        if (ActivityUtils.isActivityExistsInStack(HomeAc::class.java)) {
            return
        }
        context.startActivity(Intent(context, HomeAc::class.java).apply {
            putExtra("showBottom",showBottom)
        })
    }

    fun preLoadAllAd(){
        AdShowed.readLocalNum()
        AdShowed.reset()
        LoadAdImpl.preLoadAd()
    }

    fun readReferrer(app: Application){
        ReferrerManager.readReferrer(app)
        TbaManager.getCloak(app)
    }

    fun showOldAc()=!ReferrerManager.isBuyUser()||TbaManager.isCloak
}