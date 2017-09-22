package com.fingersoft.feature.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.lzy.okgo.OkGo


class LoginActivity : Activity(), OnClickListener, LoginManager.LoginListener {

    private var loginUserName: EditText? = null
    private var loginPassword: EditText? = null
    private var loginBtn: Button? = null
    private var loginMissps: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_login)
        OkGo.init(this.application)
        initView()
    }

    // 控件的初始化
    private fun initView() {
        loginUserName = findViewById(R.id.loginId) as EditText
        loginPassword = findViewById(R.id.loginPassword) as EditText
        loginBtn = findViewById(R.id.loginBtn) as Button
        loginBtn!!.setOnClickListener(this)
        loginMissps = findViewById(R.id.loginMissps) as Button
        loginMissps!!.setOnClickListener(this)

    }

    // 控件的点击事件
    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginBtn -> {
                val userName = loginUserName!!.text.toString()
                val password = loginPassword!!.text.toString()
                LoginManager.instance.login(userName, password, this)
            }
            R.id.loginMissps -> {
                Toast.makeText(this, "功能开发中...", Toast.LENGTH_SHORT).show()
            }
            else -> {
            }
        }

    }

    override fun onLoginSuccess(response: String) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
    }

    override fun onLoginFail(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

}
