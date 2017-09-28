package com.fingersoft.feature.lock

import android.app.Activity
import android.app.Fragment
import android.app.FragmentContainer
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.fingersoft.feature.actionsheet.ActionSheetDialog


/**
 * Created by Administrator on 2017/9/26.
 */
class LockActivity : Activity(), ActionSheetDialog.MenuListener {


    var btn_changeLoginType: TextView? = null
    var isReset: Boolean = false;
    var type: String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lock)
        initView();
    }

    private fun initView() {
        btn_changeLoginType = findViewById(R.id.change_login_type) as TextView
        isReset = intent.getBooleanExtra("isReset", false)
        type = intent.getStringExtra("type")
        setDefaultFragment()
        val menuView = ActionSheetDialog(this)
        menuView.addMenuItem("密码登录").addMenuItem("手势解锁").addMenuItem("指纹解锁")
        menuView.setCancelText("取消")
        menuView.setMenuListener(this)
        btn_changeLoginType?.setOnClickListener {
            menuView.show()
        }
    }


    private fun setDefaultFragment() {
        var fragment: Fragment? = null;
        var args = Bundle();
        when (type) {
            "relogin" -> {
                fragment = com.fingersoft.feature.login.LoginFragment();
            }
            "gesture" -> {
                fragment = com.fingersoft.feature.lock.gesture.GestureLockFragment();
                args.putBoolean("isReset", isReset)
            }
            "fingerprint" -> {

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
                fragment = com.fingersoft.feature.login.LoginFragment();
            }
            "手势解锁" -> {
                fragment = com.fingersoft.feature.lock.gesture.GestureLockFragment();
                args.putBoolean("isReset", isReset)
            }
            "指纹解锁" -> {

            }
        }
        setFragment(fragment, args)
    }

    override fun onCancel() {

    }
}