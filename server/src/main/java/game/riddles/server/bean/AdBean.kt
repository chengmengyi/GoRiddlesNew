package game.riddles.server.bean

import androidx.annotation.Keep

@Keep
class AdBean(
    val from:String,
    val data_id:String,
    val type:String,
    val index:Int
) {
    override fun toString(): String {
        return "AdBean(from='$from', data_id='$data_id', type='$type', index=$index)"
    }
}