package game.riddles.core.utils

import com.blankj.utilcode.util.GsonUtils
import com.squareup.moshi.Moshi
import java.io.Serializable

inline fun <reified T : Serializable> T.toJson(): String {
    return runCatching {
        Moshi.Builder()
            .build()
            .adapter(T::class.java)
            .toJson(this)
    }.getOrElse { "" }
}

inline fun <reified T : Serializable> String?.toSerializableByJson(): T? {
    if (this.isNullOrBlank()) return null
    return runCatching {
        Moshi.Builder()
            .build()
            .adapter(T::class.java)
            .fromJson(this)
    }.getOrNull()
}

fun Any?.toJson(): String {
    return runCatching { GsonUtils.toJson(this) }.getOrElse { "" }
}