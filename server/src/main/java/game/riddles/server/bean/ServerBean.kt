package game.riddles.server.bean

import androidx.annotation.Keep
import com.github.shadowsocks.database.Profile
import com.github.shadowsocks.database.ProfileManager

@Keep
class ServerBean(
    val adjectives: String?="",
    val canal: String?="",
    val departure: String?="",
    val map: String?="Faster server",
    val mouths: String?="",
    val office: Int?=0,
    val probe: String?="",
    val thresholds: String?="",
    val volts: String?="",
){
    fun isFast()=map=="Faster server"

    fun getServerId():Long{
        ProfileManager.getActiveProfiles()?.forEach {
            if (it.host==canal&&it.remotePort==office){
                return it.id
            }
        }
        return 0L
    }

    fun writeServerId(){
        val profile = Profile(
            id = 0L,
            name = "$map - $volts",
            host = canal?:"",
            remotePort = office?:0,
            password = thresholds?:"",
            method = adjectives?:""
        )
        var id:Long?=null
        ProfileManager.getActiveProfiles()?.forEach {
            if (it.remotePort==profile.remotePort&&it.host==profile.host){
                id=it.id
                return@forEach
            }
        }
        if (null==id){
            ProfileManager.createProfile(profile)
        }else{
            profile.id=id!!
            ProfileManager.updateProfile(profile)
        }
    }
}