package com.fingersoft.feature.lock.gesture

import android.annotation.SuppressLint
import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import com.fingersoft.feature.lock.LockManager
import com.fingersoft.feature.lock.R
import com.fingersoft.feature.lock.view.listener.LockListener
import com.fingersoft.feature.lock.gesture.listener.GesturePasswordSettingListener


/**
 * Created by Administrator on 2017/9/25.
 */
class GestureLockFragment : Fragment(), GestureLockViewGroup.GestureLockViewLoad {

    lateinit var mGestureLockViewGroup: GestureLockViewGroup;
//    lateinit var tv_state: TextView;
    private var isReset = false
    private var lockView: View? = null
    lateinit var lockListener: LockListener;

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lockView = inflater!!.inflate(R.layout.activity_gesturelock, container, false)
        initGesture()
        return lockView as View?
    }

    override fun loadSuccess() {
        setGestureWhenNoSet()
    }

    private fun initGesture() {
//        tv_state = lockView?.findViewById(R.id.tv_state) as TextView
        mGestureLockViewGroup = lockView?.findViewById(R.id.gesturelock) as GestureLockViewGroup
        mGestureLockViewGroup.setGestureLoadListener(this)
        if (LockManager.lockListener != null) {
            lockListener = LockManager.lockListener as LockListener
        }
        gestureEventListener()
        gesturePasswordSettingListener()
        gestureRetryLimitListener()
    }


    private fun resetGesturePattern() {
        mGestureLockViewGroup.removePassword()
        setGestureWhenNoSet()
        mGestureLockViewGroup.resetView()
    }


    @SuppressLint("LongLogTag")
    private fun gestureEventListener() {
        mGestureLockViewGroup.setGestureEventListener { matched ->
            Log.d("onGestureEvent matched: ", matched.toString())
            if (!matched) {
//                tv_state.setTextColor(Color.RED)
//                tv_state.setText("手势密码错误")
                lockListener.onGustureMatchError()
            } else {
                if (isReset) {
                    isReset = false
                    resetGesturePattern()
                    lockListener.onGustureRemoveCallBack()
                } else {
//                    tv_state.setTextColor(Color.BLUE)
//                    tv_state.setText("手势密码正确")
                    lockListener.onGustureMatchSuccess()
                }
            }
        }
    }

    private fun gesturePasswordSettingListener() {
        mGestureLockViewGroup.setGesturePasswordSettingListener(object : GesturePasswordSettingListener {
            override fun onFirstInputComplete(len: Int): Boolean {
                Log.d("onFirstInputComplete", "onFirstInputComplete")
                if (len > 3) {
//                    tv_state.setTextColor(Color.BLUE)
//                    tv_state.setText("再次绘制手势密码")
                    return true
                } else {
//                    tv_state.setTextColor(Color.RED)
//                    tv_state.setText("最少连接4个点，请重新输入!")
                    return false
                }
            }

            override fun onSuccess() {
                Log.d("onSuccess", "onSuccess")
//                tv_state.setTextColor(Color.BLUE)
//                tv_state.setText("密码设置成功")
                lockListener.onGustureSetSuccess()
            }

            override fun onFail() {
                Log.d("onFail", "onFail")
//                tv_state.setTextColor(Color.RED)
//                tv_state.setText("与上一次绘制不一致，请重新绘制")
            }
        })
    }

    private fun gestureRetryLimitListener() {
        mGestureLockViewGroup.setGestureUnmatchedExceedListener(3) {
//            tv_state.setTextColor(Color.RED)
//            tv_state.setText("错误次数过多，请稍后再试")
            mGestureLockViewGroup.resetView()
            lockListener.onGustureRetryLimitCallBack()
        }
    }

    private fun setGestureWhenNoSet() {
        if (!isReset) {
            if (!mGestureLockViewGroup.isSetPassword) {
//                tv_state.setTextColor(Color.BLUE)
//                tv_state.setText("绘制手势密码")
            } else {
//                tv_state.setTextColor(Color.BLUE)
//                tv_state.setText("请输入手势密码解锁")
            }
        } else {
            if (!mGestureLockViewGroup.isSetPassword) {
//                tv_state.setTextColor(Color.BLUE)
//                tv_state.setText("开始设置密码")
            } else {
                isReset = true
//                tv_state.setTextColor(Color.BLUE)
//                tv_state.setText("请输入原手势密码")
                mGestureLockViewGroup.resetView()
            }
        }
    }

}