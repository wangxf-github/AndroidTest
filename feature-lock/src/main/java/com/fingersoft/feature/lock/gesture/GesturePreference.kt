package com.fingersoft.feature.lock.gesture

import android.content.Context
import android.content.SharedPreferences

/**
 * 项目名称：MeshLed_dxy
 * 类描述：
 * 创建人：oden
 * 创建时间：2016/7/25 19:05
 */
class GesturePreference(private val context: Context, nameTableId: Int) {
    private val fileName = "com.oden.gesturelock.filename"
    private var nameTable = "com.oden.gesturelock.nameTable"

    init {
        if (nameTableId != -1)
            this.nameTable = nameTable + nameTableId
    }

    fun WriteStringPreference(data: String) {
        val preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(nameTable, data)
        editor.commit()
    }

    fun ReadStringPreference(): String {
        val preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        return preferences.getString(nameTable, "null")
    }

}
