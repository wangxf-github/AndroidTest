package com.fingersoft.feature.lock.fingerprint
import android.app.Dialog
import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.fingersoft.feature.lock.R
import com.lzy.okgo.OkGo
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.TranslateAnimation
import com.fingersoft.feature.lock.LockManager
import com.fingersoft.feature.lock.view.listener.LockListener


class FingerPrintFragment : Fragment(){

    public var lockListener: LockListener? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    // 控件的初始化
    private fun initView() {
        lockListener = LockManager.lockListener
        LockContext.init(activity.applicationContext)
        if(!FingerprintUtils.reject_if_need()){
            var dialog :Dialog = createLoadingDialog(activity);
            var fingerprintCore :FingerprintUtils.FingerprintCore = FingerprintUtils.FingerprintCore(activity.applicationContext)
            fingerprintCore.setFingerprintManager(object : FingerprintUtils.FingerprintCore.IFingerprintResultListener {
                override fun onAuthenticateSuccess() {
                    lockListener?.onFingerPrintMatchSuccess()
                    dialog.dismiss()
                    fingerprintCore.cancelAuthenticate()
                }

                override fun onAuthenticateFailed(helpId: Int) {
                    val animation = TranslateAnimation(10f, -10f, 0f, 0f)
                    animation.interpolator = OvershootInterpolator()
                    animation.duration = 50
                    animation.repeatCount = 2
                    animation.repeatMode = Animation.REVERSE
                    v!!.startAnimation(animation)
                    lockListener?.onFingerPrintMatchFail()
                }

                override fun onAuthenticateError(errMsgId: Int) {
                    val animation = TranslateAnimation(10f, -10f, 0f, 0f)
                    animation.interpolator = OvershootInterpolator()
                    animation.duration = 50
                    animation.repeatCount = 2
                    animation.repeatMode = Animation.REVERSE
                    v!!.startAnimation(animation)
                    lockListener?.onFingerPrintMatchError()
                }

                override fun onStartAuthenticateResult(isSuccess: Boolean) {
                    if(!dialog.isShowing){
                        dialog.show()
                    }
                }

            })
            fingerprintCore.startAuthenticate()
        }
    }

    /**
     * 得到自定义的progressDialog

     * @param context
     * *
     * @param msg
     * *
     * @return
     */
    var v:View? = null;
    fun createLoadingDialog(context: Context): Dialog {
        val inflater = LayoutInflater.from(context)
        v = inflater.inflate(R.layout.finger_dialog, null)// 得到加载view
        val layout = v!!.findViewById(R.id.dialog_view) as LinearLayout// 加载布局
        val fingerDialog = Dialog(context, R.style.finger_dialog)// 创建自定义样式dialog
        fingerDialog.setCancelable(true)// 可以用“返回键”取消
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT)
        fingerDialog.setContentView(layout, layoutParams)// 设置布局
        fingerDialog.setCanceledOnTouchOutside(false)
        return fingerDialog
    }

}
