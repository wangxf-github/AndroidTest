package com.fingersoft.im

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.fingersoft.feature.lock.LockManager
import com.fingersoft.feature.lock.view.listener.LockListener
import com.fingersoft.feature.login.R

/**
 * Created by Administrator on 2017/9/25.
 */
class MainActivity : Activity(), View.OnClickListener,LockListener {
    override fun onGustureRetryLimitCallBack() {
    }

    override fun onGustureMatchSuccess() {
    }

    override fun onGustureMatchError() {
    }

    override fun onGustureSetSuccess() {
    }

    override fun onGustureRemoveCallBack() {
    }

    override fun onLoginSuccess(success: String) {
    }

    override fun onLoginFail(fail: String) {
    }

    override fun onFingerPrintMatchFail() {
        Log.d("Main","FAIL....");
    }

    override fun onFingerPrintMatchSuccess() {
        Toast.makeText(this,"指纹识别成功",Toast.LENGTH_SHORT).show()
    }

    override fun onFingerPrintMatchError() {
        Toast.makeText(this,"失败次数过多,请稍后再试",Toast.LENGTH_SHORT).show()

    }

    lateinit var btn_login: Button;
    lateinit var btn_gestureLock: Button;
    lateinit var btn_gesturereset: Button;
    lateinit var btn_fingerprint: Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        btn_login = findViewById(R.id.btn_login) as Button
        btn_gesturereset = findViewById(R.id.btn_gesturereset) as Button
        btn_fingerprint = findViewById(R.id.btn_finger) as Button
        btn_gestureLock = findViewById(R.id.btn_gesturelock) as Button
        btn_login.setOnClickListener(this)
        btn_gestureLock.setOnClickListener(this)
        btn_gesturereset.setOnClickListener(this)
        btn_fingerprint.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_login -> {
                var bitmap:Bitmap = BitmapFactory.decodeResource(resources,R.drawable.user_qq)
                LockManager.instance.doReLoginLock(v.context, "123123", bitmap,this)
            }
            R.id.btn_finger -> {
            LockManager.instance.doFingerprintLock(v.context,null,this)
            }
            R.id.btn_gesturelock -> {
                LockManager.instance.doGestureLock(v.context, null,this)
            }
            R.id.btn_gesturereset -> {
                LockManager.instance.doResetGesturePassword(v.context,null, this)
            }
        }
    }


}