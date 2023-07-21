package bali.rahul.ovale.storage

import android.content.Context

class Storage(context: Context) {

    private val sharedPrefs = context.getSharedPreferences("OvalePrefs", Context.MODE_PRIVATE)

    fun save(key: String, value: String) {
        sharedPrefs.edit().putString(key, value).apply()
    }

    fun save(key: String, value: Int) {
        sharedPrefs.edit().putInt(key, value).apply()
    }

    fun get(key: String, returnInt: Int): Any? {
        return when (returnInt) {
            0 -> sharedPrefs.getString(key, null)
            1 -> sharedPrefs.getInt(key, 0)
            else -> null
        }
    }


}

