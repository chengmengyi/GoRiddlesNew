package game.riddles.server.bean

import com.github.shadowsocks.database.Profile
import com.github.shadowsocks.database.ProfileManager

class ServerBean(
    val goRi_pwd:String="",
    val goRi_account:String="",
    val goRi_port:Int=0,
    val goRi_country:String="Faster server",
    val goRi_city:String="",
    val goRi_ip:String="",
) {
    fun isFast()=goRi_country=="Faster server"&&goRi_ip.isEmpty()

    fun getServerId():Long{
        ProfileManager.getActiveProfiles()?.forEach {
            if (it.host==goRi_ip&&it.remotePort==goRi_port){
                return it.id
            }
        }
        return 0L
    }

    fun writeServerId(){
        val profile = Profile(
            id = 0L,
            name = "$goRi_country - $goRi_city",
            host = goRi_ip,
            remotePort = goRi_port,
            password = goRi_pwd,
            method = goRi_account
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