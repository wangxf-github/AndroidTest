package com.fingersoft.feature.login

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.fingersoft.feature.lock.LockManager
import com.fingersoft.feature.lock.R
import com.lzy.okgo.OkGo


class LoginFragment : Fragment(), OnClickListener {

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
//                val userName = loginUserName?.text.toString()
                val password = loginPassword?.text.toString()
                LockManager.instance.login(null, password)
            }
//            R.id.loginMissps -> {
//                Toast.makeText(this.activity.applicationContext, "功能开发中...", Toast.LENGTH_SHORT).show()
//            }
            else -> {
            }
        }
    }
}
