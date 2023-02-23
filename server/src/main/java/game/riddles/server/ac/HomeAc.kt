package game.riddles.server.ac

import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.VpnService
import android.view.Gravity
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.core.animation.doOnEnd
import com.blankj.utilcode.util.SizeUtils
import com.github.shadowsocks.utils.StartService
import game.riddles.server.R
import game.riddles.server.admob.LoadAdImpl
import game.riddles.server.admob.ShowFullAd
import game.riddles.server.admob.ShowNativeAd
import game.riddles.server.base.BaseAc
import game.riddles.server.bean.ServerBean
import game.riddles.server.conf.Fire
import game.riddles.server.conf.Local
import game.riddles.server.server.ConnectServer
import game.riddles.server.server.ConnectTimeManager
import game.riddles.server.util.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_home_content.*
import kotlinx.android.synthetic.main.layout_home_drawer.*
import java.lang.Exception
import kotlin.system.exitProcess

class HomeAc: BaseAc(R.layout.activity_home), ConnectServer.IConnectCallback, ConnectTimeManager.ITimerCallback {
    private var canClick=true
    private var permission=false
    private var connect=true
    private var showBottom=false
    private val instance= SizeUtils.dp2px(91F)
    private var connectAnimator:ValueAnimator?=null

//    private val showConnectAd by lazy { ShowFullAd(this, Local.CONNECT) }
//    private val showHomeAd by lazy { ShowNativeAd(Local.HOME,this) }

    private val registerResult=registerForActivityResult(StartService()) {
        if (!it && permission) {
            permission = false
            connectServer()
        } else {
            canClick=true
            showToast("Connected Fail")
        }
    }


    override fun initView() {
        iv_guide.show(AdShowed.firstLoad)
        immersionBar.statusBarView(top_view).init()
        showBottom=intent.getBooleanExtra("showBottom",false)
        llc_bottom.show(showBottom)
        ConnectServer.init(this,this)
        ConnectTimeManager.setTimerCallback(this)
        setClick()
        isIRUser()
    }

    private fun setClick(){
        iv_connect_btn.setOnClickListener {
            if (drawer_layout.isOpen) return@setOnClickListener
            clickConnect()
        }

        iv_connect_btn_bg.setOnClickListener { iv_connect_btn.performClick() }

        iv_guide.setOnClickListener { iv_connect_btn.performClick() }

        iv_choose_server.setOnClickListener {
            if (!drawer_layout.isOpen&&!AdShowed.firstLoad&&canClick){
                startActivityForResult(Intent(this, ChooseServerAc::class.java),1000)
            }
        }

        iv_choose.setOnClickListener { iv_choose_server.performClick() }

        iv_set.setOnClickListener {
            if (!drawer_layout.isOpen&&!AdShowed.firstLoad&&canClick){
                drawer_layout.openDrawer(Gravity.LEFT)
            }
        }

        llc_contact.setOnClickListener {
            try {
                val uri = Uri.parse("mailto:${Local.EMAIL}")
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                startActivity(intent)
            }catch (e: Exception){
                showToast("Contact us by email：${Local.EMAIL}")
            }
        }

        iv_agree.setOnClickListener { startActivity(Intent(this, WebAc::class.java)) }

        iv_share.setOnClickListener {
            val pm = packageManager
            val packageName=pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).packageName
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=${packageName}"
            )
            startActivity(Intent.createChooser(intent, "share"))
        }

        llc_riddles.setOnClickListener {
            if (!drawer_layout.isOpen&&!AdShowed.firstLoad&&canClick){
                onBackPressed()
            }
        }
    }

    private fun clickConnect(){
        LoadAdImpl.load(Local.CONNECT)
        LoadAdImpl.load(Local.RESULT)
        if (!canClick) return
        canClick=false
        if (AdShowed.firstLoad){
            AdShowed.firstLoad=false
            iv_guide.show(AdShowed.firstLoad)
        }
        if (ConnectServer.isConnected()){
            ConnectServer.disconnect()
            updateDisConnectingUI()
            startConnectAnimator(false)
        }else{
            updateServerInfo()
            if (netStatus()==1){
                showNoNetDialog()
                canClick=true
                return
            }
            if (VpnService.prepare(this) != null) {
                permission = true
                registerResult.launch(null)
                return
            }

            connectServer()
        }
    }

    private fun connectServer(){
        updateConnectingUI()
        ConnectServer.getServerInfo(supportFragmentManager) {
            ConnectServer.connect()
            startConnectAnimator(true)
        }
    }

    private fun startConnectAnimator(connect:Boolean){
        this.connect=connect
        connectAnimator= ValueAnimator.ofInt(0, 100).apply {
            duration=3000L
            interpolator = LinearInterpolator()
            addUpdateListener {
                val pro = it.animatedValue as Int
                iv_connect_btn.translationY=getTranslationY(connect, pro)
//                val duration = (10 * (pro / 100.0F)).toInt()
//                if (duration in 2..9){
//                    showConnectAd.show(
//                        showing = {
//                            stopConnectAnimator()
//                            checkConnectResult(jump = false)
//                            iv_connect_btn.translationY=getTranslationY(connect, 100)
//                        },
//                        close = {
//                            checkConnectResult()
//                        }
//                    )
//                }else if (duration>=10){
//                    checkConnectResult()
//                }
            }
            doOnEnd { checkConnectResult() }
            start()
        }
    }

    private fun checkConnectResult(jump:Boolean=true){
        val bool=if(connect) ConnectServer.isConnected() else ConnectServer.isDisconnected()
        if (bool){
            if (connect){
                updateConnectedUI()
            }else{
                updateStoppedUI()
                updateServerInfo()
            }
            jumpToResult(jump)
        }else{
            updateStoppedUI()
            showToast(if (connect) "Connect Fail" else "Disconnect Fail")
            if (connect){
                iv_connect_btn.translationY=0F
            }
        }
        canClick=true
    }

    private fun jumpToResult(jump:Boolean){
        if(AcRegister.goFront&&jump){
            startActivity(Intent(this, ResultAc::class.java).apply {
                putExtra("connect",connect)
            })
        }
    }

    private fun getTranslationY(connect: Boolean,pro:Int):Float{
        val fl = instance/ 100F * pro
        return if (connect) fl else instance-fl
    }


    private fun stopConnectAnimator(){
        connectAnimator?.removeAllUpdateListeners()
        connectAnimator?.cancel()
        connectAnimator=null
    }


    private fun updateServerInfo(){
        val currentServer = ConnectServer.currentServer
        tv_name.text=if(currentServer.isFast()) currentServer.map else "${currentServer.map} - ${currentServer.volts}"
        iv_logo.setImageResource(getServerLogo(currentServer.map?:""))
    }

    private fun updateConnectingUI(){
        tv_connect_status_text.isSelected=true
        iv_connect_status_image.isSelected=true
        tv_connect_status_text.text="Connecting..."
    }

    private fun updateConnectedUI(){
        tv_connect_status_text.isSelected=true
        iv_connect_status_image.isSelected=true
        tv_connect_status_text.text="Connected"
    }

    private fun updateStoppedUI(){
        tv_connect_status_text.isSelected=false
        iv_connect_status_image.isSelected=false
        tv_connect_status_text.text="Disconnect"
    }


    private fun updateDisConnectingUI(){
        tv_connect_status_text.isSelected=true
        iv_connect_status_image.isSelected=true
        tv_connect_status_text.text="Disconnecting..."
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==1000){
            when(data?.getStringExtra("action")){
                "disconnect"->{
                    clickConnect()
                }
                "connect"->{
                    updateServerInfo()
                    clickConnect()
                }
            }
        }
    }

    override fun connectSuccess() {
        updateConnectedUI()
    }

    override fun disconnectSuccess() {
        if(canClick){
            updateStoppedUI()
        }
    }

    override fun connectTime(time: String) {
        tv_connect_time.text=time
    }

    private fun isIRUser(){
        if (Fire.irUser){
            AlertDialog.Builder(this).apply {
                setMessage("Due to policy reasons,this service is not available in your country")
                setCancelable(false)
                setPositiveButton("sure"
                ) { p0, p1 -> exitProcess(0) }
                show()
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
//        if (!AdShowed.adShowed(Local.HOME)){
//            showHomeAd.show(showDesc = false)
//        }
//    }

    override fun onBackPressed() {
        if(iv_guide.visibility==View.VISIBLE){
            AdShowed.firstLoad=false
            iv_guide.show(false)
        }else{
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopConnectAnimator()
        ConnectServer.onDestroy()
        ConnectTimeManager.setTimerCallback(null)
//        showHomeAd.endShow()
        AdShowed.setShowed(Local.HOME,false)
    }
}