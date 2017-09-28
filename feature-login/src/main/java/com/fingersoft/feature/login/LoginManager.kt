package com.fingersoft.feature.login

import android.content.Context
import android.content.Intent
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.callback.StringCallback
import okhttp3.Call
import okhttp3.Response
import java.lang.Exception

/**
 * Created by Administrator on 2017/9/22.
 */
class LoginManager private constructor() {

    companion object {
        val instance = LoginManager();
    }

    public fun loginRequest(userName: String, password: String, loginListener: LoginListener) {
        checkParams(userName, password, loginListener)
        OkGo.post("http://192.168.2.89:8000/fg/login")     // 请求方式和请求url
                .tag(this)// 请求的 tag, 主要用于取消对应的请求
                .params("userName", userName)
                .params("password", password)
                .cacheKey("login")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .execute(object : StringCallback() {

                    override fun onSuccess(t: String?, call: Call?, response: Response?) {
                        loginListener.onLoginSuccess(t.toString())
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        super.onError(call, response, e)
                        loginListener.onLoginFail(response.toString())
                    }
                })

    }


    public fun doLoginOut(userName: String, password: String) {

    }


    private  fun checkParams(userName: String, password: String, loginListener: LoginListener) {
        if (userName == null || userName == "") {
            loginListener.onLoginFail("用户名为空!!!")
            return;
        }
        if (password == null || password == "") {
            loginListener.onLoginFail("密码为空!!!")
            return;
        }
    }

    public fun doLogin(context: Context) {
        val intent = Intent(context, LoginFragment::class.java)
        context.startActivity(intent)
    }

    interface LoginListener {
        fun onLoginSuccess(success: String);
        fun onLoginFail(fail: String);
    }
}