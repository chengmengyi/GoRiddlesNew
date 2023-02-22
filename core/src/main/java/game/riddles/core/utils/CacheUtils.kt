package game.riddles.core.utils

import com.blankj.utilcode.util.SPUtils
import kotlin.reflect.KProperty

fun writeBooleanToCache(key: String, value: Boolean) {
    getSpUtils().put(key.toUniqueKey(), value)
}

fun readBooleanFromCache(key: String): Boolean {
    return getSpUtils().getBoolean(key.toUniqueKey(), false)
}

fun writeIntToCache(key: String, value: Int) {
    getSpUtils().put(key.toUniqueKey(), value)
}

fun readIntFromCache(key: String, def: Int = 0): Int {
    return getSpUtils().getInt(key.toUniqueKey(), def)
}

fun writeStringToCache(key: String, value: String) {
    getSpUtils().put(key.toUniqueKey(), value)
}

fun readStringFromCache(key: String, def: String = ""): String {
    return getSpUtils().getString(key.toUniqueKey(), def)
}

private fun getSpUtils(): SPUtils {
    return SPUtils.getInstance("GoRiddles")
}

class CacheableBooleanDelegate(private val keyGenerator: () -> String) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean {
        return readBooleanFromCache(keyGenerator())
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
        writeBooleanToCache(keyGenerator(), value)
    }
}

class CacheableIntDelegate(private val def: Int = 0, private val keyGenerator: () -> String) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return readIntFromCache(keyGenerator(), def = def)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        writeIntToCache(keyGenerator(), value)
    }
}

class CacheableStringDelegate(
    private val def: String = "",
    private val keyGenerator: () -> String
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return readStringFromCache(keyGenerator(), def = def)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        writeStringToCache(keyGenerator(), value)
    }
}