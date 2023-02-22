package game.riddles.server.server

import com.github.shadowsocks.Core
import com.github.shadowsocks.aidl.IShadowsocksService
import com.github.shadowsocks.aidl.ShadowsocksConnection
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.preference.DataStore
import game.riddles.server.base.BaseAc
import game.riddles.server.bean.ServerBean
import kotlinx.coroutines.GlobalScope
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
        GlobalScope.launch {
            if (currentServer.isFast()){
                fastServer = ServerInfoManager.getFastServer()
                DataStore.profileId = fastServer.getServerId()
            }else{
                DataStore.profileId = currentServer.getServerId()
            }
            Core.startService()
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