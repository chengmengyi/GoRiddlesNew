package game.riddles.server.server

import kotlinx.coroutines.*
import java.lang.Exception

object ConnectTimeManager {
    private var time=0L
    private var job:Job?=null
    private var iTimerCallback: ITimerCallback?=null


    fun reset(){
        time =0L
    }

    fun start(){
        if (null!= job) return
        job = GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                iTimerCallback?.connectTime(transTime(time))
                time++
                delay(1000L)
            }
        }
    }


    fun stop(){
        job?.cancel()
        job =null
    }

   private fun transTime(t:Long):String{
        try {
            val shi=t/3600
            val fen= (t % 3600) / 60
            val miao= (t % 3600) % 60
            val s=if (shi<10) "0${shi}" else shi
            val f=if (fen<10) "0${fen}" else fen
            val m=if (miao<10) "0${miao}" else miao
            return "${s}:${f}:${m}"
        }catch (e: Exception){}
        return "00:00:00"
    }

    fun setTimerCallback(iTimerCallback: ITimerCallback?){
        ConnectTimeManager.iTimerCallback =iTimerCallback
    }

    interface ITimerCallback {
        fun connectTime(time:String)
    }
}