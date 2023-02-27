package game.riddles.server.util

import android.content.Context
import android.net.ConnectivityManager
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import game.riddles.server.R

fun logGo(string: String){
    Log.e("qwer",string)
}

fun Context.showToast(s: String){
    Toast.makeText(this, s, Toast.LENGTH_LONG).show()
}

fun View.show(show:Boolean){
    visibility=if (show) View.VISIBLE else View.GONE
}

fun getServerLogo(string: String)=when(string){
    "Australia"-> R.drawable.australia
    "Belgium"->R.drawable.belgium
    "Brazil"->R.drawable.brazil
    "Canada"->R.drawable.canada
    "France"->R.drawable.france
    "Germany"->R.drawable.germany
    "India"->R.drawable.india
    "Ireland"->R.drawable.ireland
    "Italy"->R.drawable.italy
    "Japan"->R.drawable.japan
    "KoreaSouth"->R.drawable.koreasouth
    "Netherlands"->R.drawable.netherlands
    "NewZealand"->R.drawable.newzealand
    "Norway"->R.drawable.norway
    "Singapore"->R.drawable.singapore
    "Switzerland"->R.drawable.switzerland
    "United States"->R.drawable.unitedstates
    "United Kingdom"->R.drawable.unitedkingdom
    else-> R.drawable.fast
}


fun Context.netStatus(): Int {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
        if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
            return 2
        } else if (activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
            return 0
        }
    } else {
        return 1
    }
    return 1
}

fun Context.showNoNetDialog(){
    AlertDialog.Builder(this).apply {
        setMessage("You are not currently connected to the network")
        setPositiveButton("sure", null)
        show()
    }
}

fun String.limitArea()=contains("IR")||contains("MO")||contains("HK")||contains("CN")


