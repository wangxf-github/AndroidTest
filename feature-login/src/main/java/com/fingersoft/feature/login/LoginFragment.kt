package com.fingersoft.feature.login

import android.app.Activity
import android.app.Application
import android.app.Fragment
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.lzy.okgo.OkGo


class LoginFragment : Fragment(), OnClickListener, LoginManager.LoginListener {

//    private var loginUserName: EditText? = null
    private var loginPassword: EditText? = null
    private var loginBtn: Button? = null
    private var loginMissps: Button? = null
    private var reloginView: View? = null;


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        reloginView = inflater?.inflate(R.layout.activity_login, container, false)
        OkGo.init(activity.application)
        initView()
        return reloginView
    }

    // 控件的初始化
    private fun initView() {
//        loginUserName = reloginView?.findViewById(R.id.loginId) as EditText
        loginPassword = reloginView?.findViewById(R.id.loginPassword) as EditText
        loginBtn = reloginView?.findViewById(R.id.loginBtn) as Button
        loginBtn!!.setOnClickListener(this)
//        loginMissps = reloginView?.findViewById(R.id.loginMissps) as Button
//        loginMissps!!.setOnClickListener(this)

    }

    // 控件的点击事件
    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginBtn -> {
//                val userName = loginUserName!!.text.toString()
                val password = loginPassword!!.text.toString()
                LoginManager.instance.loginRequest(null!!, password, this)
            }
//            R.id.loginMissps -> {
//                Toast.makeText(this.activity.applicationContext, "功能开发中...", Toast.LENGTH_SHORT).show()
//            }
            else -> {
            }
        }

    }

    override fun onLoginSuccess(response: String) {
        Toast.makeText(this.activity.applicationContext, response, Toast.LENGTH_SHORT).show()
        var sh: SharedPreferences = this.activity.getSharedPreferences(LoginConfig.FG_LOGIN, android.content.Context.MODE_PRIVATE);
        var edit: SharedPreferences.Editor = sh.edit();
        edit.putString(LoginConfig.LOGIN_TOKEN, response)
        edit.commit()

    }

    override fun onLoginFail(error: String) {
        Toast.makeText(this.activity.applicationContext, error, Toast.LENGTH_SHORT).show()
    }

}
