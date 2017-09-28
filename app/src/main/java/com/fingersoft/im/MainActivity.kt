package com.fingersoft.im

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.fingersoft.feature.lock.LockManager
import com.fingersoft.feature.lock.view.listener.LockListener
import com.fingersoft.feature.login.R

/**
 * Created by Administrator on 2017/9/25.
 */
class MainActivity : Activity(), View.OnClickListener {

    lateinit var btn_login: Button;
    lateinit var btn_gestureLock: Button;
    lateinit var btn_gesturereset: Button;
    lateinit var btn_lock: Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        btn_login = findViewById(R.id.btn_login) as Button
        btn_gesturereset = findViewById(R.id.btn_login) as Button
        btn_gesturereset = findViewById(R.id.btn_gesturereset) as Button
        btn_gestureLock = findViewById(R.id.btn_lock) as Button
        btn_lock = findViewById(R.id.btn_gesturelock) as Button
        btn_login.setOnClickListener(this)
        btn_gestureLock.setOnClickListener(this)
        btn_gesturereset.setOnClickListener(this)
        btn_lock.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_login -> {
                LockManager.instance.doReLoginLock(v.context, "123123", object : LockListener {
                    override fun onGustureRetryLimitCallBack() {
                        Toast.makeText(this@MainActivity, "密码尝试超次数!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onGustureMatchSuccess() {
                        Toast.makeText(this@MainActivity, "密码验证成功!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onGustureMatchError() {
                        Toast.makeText(this@MainActivity, "密码验证失败!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onGustureSetSuccess() {
                        Toast.makeText(this@MainActivity, "密码设置成功!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onGustureRemoveCallBack() {
                        Toast.makeText(this@MainActivity, "密码删除成功!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onLoginSuccess(success: String) {
                        Toast.makeText(this@MainActivity, success, Toast.LENGTH_SHORT).show()
                    }

                    override fun onLoginFail(fail: String) {
                        Toast.makeText(this@MainActivity, fail, Toast.LENGTH_SHORT).show()
                    }

                })
            }
            R.id.btn_lock -> {
//                var intent = Intent(this@MainActivity, LockActivity::class.java);
//                startActivity(intent)
            }
            R.id.btn_gesturelock -> {
                LockManager.instance.doGestureLock(v.context, object : LockListener {
                    override fun onLoginSuccess(success: String) {
                    }

                    override fun onLoginFail(fail: String) {
                    }

                    override fun onGustureSetSuccess() {
                        Toast.makeText(this@MainActivity, "密码设置成功!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onGustureRemoveCallBack() {
                        Toast.makeText(this@MainActivity, "密码删除成功!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onGustureRetryLimitCallBack() {
                        Toast.makeText(this@MainActivity, "密码尝试超次数!", Toast.LENGTH_SHORT).show()

                    }

                    override fun onGustureMatchSuccess() {
                        Toast.makeText(this@MainActivity, "密码验证成功!", Toast.LENGTH_SHORT).show()

                    }

                    override fun onGustureMatchError() {
                        Toast.makeText(this@MainActivity, "密码验证失败!", Toast.LENGTH_SHORT).show()
                    }

                })
            }
            R.id.btn_gesturereset -> {
                LockManager.instance.doResetGesturePassword(v.context, object : LockListener {
                    override fun onLoginSuccess(success: String) {
                    }

                    override fun onLoginFail(fail: String) {
                    }

                    override fun onGustureSetSuccess() {
                        Toast.makeText(this@MainActivity, "密码设置成功!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onGustureRemoveCallBack() {
                        Toast.makeText(this@MainActivity, "密码删除成功!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onGustureRetryLimitCallBack() {
                        Toast.makeText(this@MainActivity, "密码尝试超次数!", Toast.LENGTH_SHORT).show()

                    }

                    override fun onGustureMatchSuccess() {
                        Toast.makeText(this@MainActivity, "密码验证成功!", Toast.LENGTH_SHORT).show()

                    }

                    override fun onGustureMatchError() {
                        Toast.makeText(this@MainActivity, "密码验证失败!", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }


}