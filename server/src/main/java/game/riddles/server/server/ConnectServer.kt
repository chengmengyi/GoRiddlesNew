package game.riddles.server.server

import com.github.shadowsocks.Core
import com.github.shadowsocks.aidl.IShadowsocksService
import com.github.shadowsocks.aidl.ShadowsocksConnection
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.preference.DataStore
import game.riddles.server.base.BaseAc
import game.riddles.server.bean.ServerBean
import game.riddles.server.bean.ServerInfoBean
import game.riddles.server.util.logGo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ConnectServer: ShadowsocksConnection.Callback {

    private var baseAc: BaseAc?=null
    private var state = BaseService.State.Stopped
    var currentServer= ServerBean()
    var lastServer= ServerBean()
    var fastServer= ServerBean()
    private val sc= ShadowsocksConnection(true)
    private var iConnectCallback: IConnectCallback?=null

    fun init(baseAc: BaseAc, iConnectCallback: IConnectCallback){
        ConnectServer.baseAc =baseAc
        ConnectServer.iConnectCallback =iConnectCallback
        sc.connect(baseAc,this)
    }

    fun connect(){
        state = BaseService.State.Connecting
        ConnectTimeManager.reset()
        if (currentServer.isFast()){
            GlobalScope.launch {
                delay(500L)
                DataStore.profileId = fastServer.getServerId()
                logGo("=currentServer.isFast()===connect===${fastServer.getServerId()}=")
                Core.startService()
            }
        }else{
            GlobalScope.launch {
                DataStore.profileId = currentServer.getServerId()
                logGo("=chooseserver===connect=====${currentServer.getServerId()}")
                Core.startService()
            }
        }
    }

    fun getServerInfo(callback:()->Unit){
        if (currentServer.isFast()){
            if (ServerInfoManager.loadOnlineSuccess){
                ServerInfoManager.getServerInfo(null){
                    if(null!=it){
                        fastServer = it
                        callback.invoke()
                    }else{
                        state = BaseService.State.Stopped
                    }
                }
            }else{
                if(ServerInfoManager.configServerList.isNotEmpty()){
                    val random = ServerInfoManager.configServerList.random()
                    if(random.serverInfo != null){
                        fastServer = random.serverInfo
                        callback.invoke()
                    }
                }
            }
        }else{
            callback.invoke()
        }
    }

    fun disconnect(){
        state = BaseService.State.Stopping
        GlobalScope.launch {
            Core.stopService()
        }
    }

    fun isConnected()= state ==BaseService.State.Connected

    fun isDisconnected()= state ==BaseService.State.Stopped

    override fun stateChanged(state: BaseService.State, profileName: String?, msg: String?) {
        ConnectServer.state =state
        logGo("==stateChanged===${state}===")
        if (isConnected()){
            lastServer = currentServer
            ConnectTimeManager.start()
        }
        if (isDisconnected()){
            ConnectTimeManager.stop()
            iConnectCallback?.disconnectSuccess()
        }
    }

    override fun onServiceConnected(service: IShadowsocksService) {
        val state = BaseService.State.values()[service.state]
        ConnectServer.state =state
        if (isConnected()){
            lastServer = currentServer
            ConnectTimeManager.start()
            iConnectCallback?.connectSuccess()
        }
    }

    override fun onServiceDisconnected() {
        super.onServiceDisconnected()
        logGo("==onServiceDisconnected=====")
    }

    override fun onBinderDied() {
        baseAc?.let {
            sc.disconnect(it)
        }
    }

    fun onDestroy(){
        onBinderDied()
        baseAc =null
        iConnectCallback =null
    }

    interface IConnectCallback {
        fun connectSuccess()
        fun disconnectSuccess()
    }
}