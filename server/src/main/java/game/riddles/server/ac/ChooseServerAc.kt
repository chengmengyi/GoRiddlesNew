package game.riddles.server.ac

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import game.riddles.server.R
import game.riddles.server.adapter.ServerAdapter
import game.riddles.server.admob.LoadAdImpl
import game.riddles.server.admob.ShowFullAd
import game.riddles.server.base.BaseAc
import game.riddles.server.bean.ServerBean
import game.riddles.server.bean.ServerInfoBean
import game.riddles.server.bean.ServerListBean
import game.riddles.server.conf.Local
import game.riddles.server.server.ConnectServer
import game.riddles.server.server.ServerInfoManager
import game.riddles.server.util.showToast
import kotlinx.android.synthetic.main.activity_choose_server.*

class ChooseServerAc: BaseAc(R.layout.activity_choose_server) {
    private val showBackAd by lazy { ShowFullAd(this, Local.BACK) }
    private val myAdapter by lazy { ServerAdapter(this){ click(it) } }

    override fun initView() {
        LoadAdImpl.load(Local.BACK)
        immersionBar.statusBarView(top_view).init()
        iv_back.setOnClickListener { onBackPressed() }


        server_list.apply {
            layoutManager=LinearLayoutManager(this@ChooseServerAc)
            adapter=myAdapter
        }
    }

    private fun click(serverBean: ServerListBean){
        if(null!=serverBean.serverInfo){
            getServerInfoSuccess(serverBean.serverInfo,serverBean.isFast())
        }else{
            ServerInfoManager.getServerInfo(supportFragmentManager,serverBean.grinder){
                if(null!=it){
                    getServerInfoSuccess(it,serverBean.isFast())
                }else{
                    showToast("Failed to get server information")
                }
            }
        }
    }


    private fun getServerInfoSuccess(it:ServerBean,isFast:Boolean){
        val currentServer = ConnectServer.currentServer
        val connected = ConnectServer.isConnected()
        if (connected&&currentServer.canal!=it.canal){
            showDialog { chooseServer("disconnect",it,isFast) }
        }else{
            if (connected){
                chooseServer("",it,isFast)
            }else{
                chooseServer("connect",it,isFast)
            }
        }
    }

    private fun chooseServer(result:String,serverBean: ServerBean,isFast:Boolean){
        if(isFast){
            ConnectServer.currentServer=ServerBean()
        }else{
            ConnectServer.currentServer=serverBean
        }
        setResult(1000, Intent().apply {
            putExtra("action",result)
        })
        finish()
    }

    private fun showDialog(back:()->Unit){
        AlertDialog.Builder(this).apply {
            setMessage("You are currently connected and need to disconnect before manually connecting to the server.")
            setPositiveButton("sure") { _, _ ->
                back.invoke()
            }
            setNegativeButton("cancel",null)
            show()
        }
    }

    override fun onBackPressed() {
        showBackAd.show(
            emptyBack = true,
            showing = {},
            close = {
                finish()
            }
        )
    }
}