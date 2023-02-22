package game.riddles.server.tba

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun getDistinctId(context: Context)= encrypt(getAndroidId(context))

fun getAndroidId(context: Context): String {
    try {
        val id: String = Settings.Secure.getString(
            context.getContentResolver(),
            Settings.Secure.ANDROID_ID
        )
        return if ("9774d56d682e549c" == id) "" else id ?: ""
    }catch (e:Exception){

    }
    return ""
}

fun getDeviceModel()= Build.MODEL

fun getBundleId(context: Context)=context.packageName

fun getClientTs()=System.currentTimeMillis()

fun getOsVersion()= Build.VERSION.RELEASE

fun getOs()="exotic"

fun getAppVersion(context: Context)=context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_META_DATA).versionName
fun getBrand()= android.os.Build.BRAND


fun getGaid(context: Context)=try {
    AdvertisingIdClient.getAdvertisingIdInfo(context).id
}catch (e:Exception){
    Log.e("qwer","===${e.message}")
    ""
}

fun encrypt(raw: String): String {
    var md5Str = raw
    try {
        val md = MessageDigest.getInstance("MD5")
        md.update(raw.toByteArray())
        val encryContext = md.digest()
        var i: Int
        val buf = StringBuffer("")
        for (offset in encryContext.indices) {
            i = encryContext[offset].toInt()
            if (i < 0) {
                i += 256
            }
            if (i < 16) {
                buf.append("0")
            }
            buf.append(Integer.toHexString(i))
        }
        md5Str = buf.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return md5Str
}