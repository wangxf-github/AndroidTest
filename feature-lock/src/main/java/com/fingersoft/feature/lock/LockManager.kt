package com.fingersoft.feature.lock

import android.content.Context
import android.content.Intent
import com.fingersoft.feature.lock.view.listener.LockListener
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.callback.StringCallback
import okhttp3.Call
import okhttp3.Response
import java.lang.Exception


/**
 * Created by Administrator on 2017/9/25.
 */
class LockManager {


    companion object {
        public var lockListener: LockListener? = null;
        val instance = LockManager();
    }

    private var userName: String? = null;


    public fun doReLoginLock(context: Context, userName: String, listener: LockListener) {
        lockListener = listener
        this.userName = userName
        val intent = Intent(context, LockActivity::class.java)
        intent.putExtra("type", "relogin")
        context.startActivity(intent)
    }

    public fun doGestureLock(context: Context, listener: LockListener) {
        lockListener = listener
        val intent = Intent(context, LockActivity::class.java)
        intent.putExtra("type", "gesture")
        context.startActivity(intent)
    }


    public fun doResetGesturePassword(context: Context, listener: LockListener) {
        lockListener = listener
        val intent = Intent(context, LockActivity::class.java)
        intent.putExtra("isReset", true)
        intent.putExtra("type", "gesture")
        context.startActivity(intent)
    }


    public fun login(userName: String?, password: String) {
        if (password != null && password != "") {
            loginRequest(userName, password)
        } else {
            lockListener?.onLoginFail("密码为空")
        }
    }

    private fun loginRequest(userName: String?, password: String) {
        OkGo.post("http://192.168.2.89:8000/fg/login")     // 请求方式和请求url
                .tag(this)// 请求的 tag, 主要用于取消对应的请求
                .params("userName", this.userName)
                .params("password", password)
                .cacheKey("login")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .execute(object : StringCallback() {

                    override fun onSuccess(t: String?, call: Call?, response: Response?) {
                        lockListener?.onLoginSuccess(t.toString())
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        super.onError(call, response, e)
                        lockListener?.onLoginFail(response.toString())
                    }
                })

    }


}