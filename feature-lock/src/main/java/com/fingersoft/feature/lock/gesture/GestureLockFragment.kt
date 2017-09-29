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
    lateinit var gesture_state: TextView;
    private var isReset = false
    private var lockView: View? = null
    lateinit var lockListener: LockListener;

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lockView = inflater!!.inflate(R.layout.activity_gesturelock, container, false)
        initGesture()
        return lockView as View?
    }

    override fun loadSuccess() {
        isReset = arguments.getBoolean("isReset");
        setGestureWhenNoSet()
    }

    private fun initGesture() {
        gesture_state = lockView?.findViewById(R.id.gesture_state) as TextView
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
                gesture_state.setTextColor(Color.RED)
                gesture_state.setText("手势密码错误")
                lockListener.onGustureMatchError()
            } else {
                if (isReset) {
                    isReset = false
                    resetGesturePattern()
                    lockListener.onGustureRemoveCallBack()
                } else {
                    gesture_state.setTextColor(Color.BLUE)
                    gesture_state.setText("手势密码正确")
                    lockListener.onGustureMatchSuccess()
                }
            }
        }
    }

    private fun gesturePasswordSettingListener() {
        mGestureLockViewGroup.setGesturePasswordSettingListener(object : GesturePasswordSettingListener {
            override fun onFirstInputComplete(len: Int): Boolean {
                Log.d("onFirstInputComplete", "onFirstInputComplete")
                if (len > 2) {
                    gesture_state.setTextColor(Color.BLUE)
                    gesture_state.setText("再次绘制手势密码")
                    return true
                } else {
                    gesture_state.setTextColor(Color.RED)
                    gesture_state.setText("最少连接3个点，请重新输入!")
                    return false
                }
            }

            override fun onSuccess() {
                Log.d("onSuccess", "onSuccess")
                gesture_state.setTextColor(Color.BLUE)
                gesture_state.setText("密码设置成功")
                lockListener.onGustureSetSuccess()
            }

            override fun onFail() {
                Log.d("onFail", "onFail")
                gesture_state.setTextColor(Color.RED)
                gesture_state.setText("与上一次绘制不一致，请重新绘制")
            }
        })
    }

    private fun gestureRetryLimitListener() {
        mGestureLockViewGroup.setGestureUnmatchedExceedListener(5) {
            gesture_state.setTextColor(Color.RED)
            gesture_state.setText("错误次数过多，请稍后再试")
            mGestureLockViewGroup.resetView()
            lockListener.onGustureRetryLimitCallBack()
        }
    }

    private fun setGestureWhenNoSet() {
        if (!isReset) {
            if (!mGestureLockViewGroup.isSetPassword) {
                gesture_state.setTextColor(Color.BLUE)
                gesture_state.setText("绘制手势密码")
            } else {
                gesture_state.setTextColor(Color.BLUE)
                gesture_state.setText("请输入手势密码解锁")
            }
        } else {
            if (!mGestureLockViewGroup.isSetPassword) {
                gesture_state.setTextColor(Color.BLUE)
                gesture_state.setText("开始设置密码")
            } else {
                isReset = true
                gesture_state.setTextColor(Color.BLUE)
                gesture_state.setText("请输入原手势密码")
                mGestureLockViewGroup.resetView()
            }
        }
    }

}