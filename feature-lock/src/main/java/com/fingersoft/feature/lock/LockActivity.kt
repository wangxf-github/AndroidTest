package com.fingersoft.feature.lock

import android.app.Activity
import android.app.Fragment
import android.app.FragmentContainer
import android.graphics.Color
import android.os.Bundle
import android.transition.Visibility
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.fingersoft.feature.actionsheet.ActionSheetDialog


/**
 * Created by Administrator on 2017/9/26.
 */
class LockActivity : Activity(), ActionSheetDialog.MenuListener {


    var btn_changeLoginType: TextView? = null
    var loginUserAvatar: ImageView? = null
    var isReset: Boolean = false;
    var type: String? = null;
    lateinit var tv_state:TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lock)
        initView();
    }

    private fun initView() {
        loginUserAvatar = findViewById(R.id.loginUserAvatar) as ImageView
        btn_changeLoginType = findViewById(R.id.change_login_type) as TextView
        tv_state = findViewById(R.id.tv_state) as TextView
        isReset = intent.getBooleanExtra("isReset", false)
        type = intent.getStringExtra("type")
        setDefaultFragment()
        val menuView = ActionSheetDialog(this)
        val list = listOf<String>("密码登录","手势解锁","指纹解锁")
        for (title in list){
            menuView.addMenuItem(title)
        }
        menuView.setCancelText("取消")
        menuView.setMenuListener(this)
        if(LockManager.loginUserAvatar!=null){
            loginUserAvatar?.setImageBitmap(LockManager.loginUserAvatar)
        }
        btn_changeLoginType?.setOnClickListener {
            menuView.show()
        }
    }


    private fun setDefaultFragment() {
        var fragment: Fragment? = null;
        var args = Bundle();
        when (type) {
            "relogin" -> {
                tv_state.setTextColor(Color.BLUE)
                tv_state.setText("登陆")
                btn_changeLoginType?.setVisibility(View.VISIBLE)
                fragment = com.fingersoft.feature.lock.relogin.LoginFragment();
            }
            "gesture" -> {
                tv_state.setTextColor(Color.BLUE)
                tv_state.setText("手势密码")
                if(isReset){
                    btn_changeLoginType?.setVisibility(View.INVISIBLE)
                }
                fragment = com.fingersoft.feature.lock.gesture.GestureLockFragment();
                args.putBoolean("isReset", isReset)
            }
            "fingerprint" -> {
                tv_state.setTextColor(Color.BLUE)
                tv_state.setText("指纹密码")
                btn_changeLoginType?.setVisibility(View.VISIBLE)
                fragment = com.fingersoft.feature.lock.fingerprint.FingerPrintFragment();
            }
        }
        setFragment(fragment, args)
    }

    private fun setFragment(fragment: Fragment?, args: Bundle?) {
        fragment?.arguments = args
        val fm = fragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.id_content, fragment)
        transaction.commit()
    }

    override fun onItemSelected(position: Int, item: String?) {
        var fragment: Fragment? = null;
        val args = Bundle()
        when (item) {
            "密码登录" -> {
                tv_state.setTextColor(Color.BLUE)
                tv_state.setText("登陆")
                btn_changeLoginType?.setVisibility(View.VISIBLE)
                fragment = com.fingersoft.feature.lock.relogin.LoginFragment();
            }
            "手势解锁" -> {
                tv_state.setTextColor(Color.BLUE)
                tv_state.setText("手势密码")
                fragment = com.fingersoft.feature.lock.gesture.GestureLockFragment();
                args.putBoolean("isReset", isReset)
            }
            "指纹解锁" -> {
                tv_state.setTextColor(Color.BLUE)
                tv_state.setText("指纹密码")
                btn_changeLoginType?.setVisibility(View.VISIBLE)
                fragment = com.fingersoft.feature.lock.fingerprint.FingerPrintFragment();
            }
        }
        setFragment(fragment, args)
    }

    override fun onCancel() {

    }
}