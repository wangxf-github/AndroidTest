package com.fingersoft.feature.lock.fingerprint;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
//
//import com.facebook.react.bridge.Promise;
//import com.fingersoft.common.preference.UserAppPreferenceHelper;
//import com.fingersoft.feature.lock.LockContext;
//import com.fingersoft.feature.lock.PreferenceKey;
//import com.fingersoft.im.utils.LogUtils;

import java.lang.ref.WeakReference;

/**
 * Created by DD on 2017/6/24.
 */

public class FingerprintUtils {

    public static boolean reject_if_need() {
        if (!LockContext.isHardwareSupportFingerprint()) {
            Log.e("Fingerprint","硬件不支持");
            return true;
        }

        if (!LockContext.isKeyguardSecure()) {
            Log.d("Fingerprint","不处于锁屏");
            return true;
        }
        if (!LockContext.hasEnrolledFingerprints()) {
            Log.e("Fingerprint","没注册指纹");
            return true;
        }
        return false;
    }

//    public static boolean isOpened() {
//        try {
//            return userAppPreferenceHelper.getBoolean(PreferenceKey.FINGERPRINT_ENABLE, false);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public static void setOpened(boolean b) throws Exception {
//        userAppPreferenceHelper.putBoolean(PreferenceKey.FINGERPRINT_ENABLE, b);
//    }

    /**
     * Created by popfisher on 2016/11/7.
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static class FingerprintCore {

        private static final int NONE = 0;
        private static final int CANCEL = 1;
        private static final int AUTHENTICATING = 2;
        private int mState = NONE;

        private FingerprintManager mFingerprintManager;
        private WeakReference<IFingerprintResultListener> mFpResultListener;
        private CancellationSignal mCancellationSignal;
        private CryptoObjectCreator mCryptoObjectCreator;
        private FingerprintManager.AuthenticationCallback mAuthCallback;

        private int mFailedTimes = 0;
        private boolean isSupport = false;
        private Handler mHandler = new Handler(Looper.getMainLooper());

        /**
         * 指纹识别回调接口
         */
        public interface IFingerprintResultListener {
            /** 指纹识别成功 */
            void onAuthenticateSuccess();

            /** 指纹识别失败 */
            void onAuthenticateFailed(int helpId);

            /** 指纹识别发生错误-不可短暂恢复 */
            void onAuthenticateError(int errMsgId);

            /** 开始指纹识别监听成功 */
            void onStartAuthenticateResult(boolean isSuccess);
        }

        public FingerprintCore(Context context) {
            mFingerprintManager = getFingerprintManager(context);
            isSupport = (mFingerprintManager != null && isHardwareDetected());
            System.out.print("fingerprint isSupport: " + isSupport);
            initCryptoObject();
        }

        private void initCryptoObject() {
            try {
                mCryptoObjectCreator = new CryptoObjectCreator(new CryptoObjectCreator.ICryptoObjectCreateListener() {
                    @Override
                    public void onDataPrepared(FingerprintManager.CryptoObject cryptoObject) {
                        // startAuthenticate(cryptoObject);
                        // 如果需要一开始就进行指纹识别，可以在秘钥数据创建之后就启动指纹认证
                    }
                });
            } catch (Throwable throwable) {
//                LogUtils.e("create cryptoObject failed!");
            }
        }

        public void setFingerprintManager(IFingerprintResultListener fingerprintResultListener) {
            mFpResultListener = new WeakReference<IFingerprintResultListener>(fingerprintResultListener);
        }

        public void startAuthenticate() {
            startAuthenticate(mCryptoObjectCreator.getCryptoObject());
        }

        public boolean isAuthenticating() {
            return mState == AUTHENTICATING;
        }

        private void startAuthenticate(FingerprintManager.CryptoObject cryptoObject) {
            prepareData();
            mState = AUTHENTICATING;
            try {
                mFingerprintManager.authenticate(cryptoObject, mCancellationSignal, 0, mAuthCallback, null);
                notifyStartAuthenticateResult(true, "");
            } catch (Exception e) {
                try {
                    mFingerprintManager.authenticate(null, mCancellationSignal, 0, mAuthCallback, null);
                    notifyStartAuthenticateResult(true, "");
                } catch (Exception e2) {
                    notifyStartAuthenticateResult(false, Log.getStackTraceString(e2));
                }
            }
        }

        private void notifyStartAuthenticateResult(boolean isSuccess, String exceptionMsg) {
            if (isSuccess) {
                System.out.print("start authenticate...");
                if (mFpResultListener.get() != null) {
                    mFpResultListener.get().onStartAuthenticateResult(true);
                }
            } else {
                System.out.print("startListening, Exception" + exceptionMsg);
                if (mFpResultListener.get() != null) {
                    mFpResultListener.get().onStartAuthenticateResult(false);
                }
            }
        }

        private void notifyAuthenticationSucceeded() {
            System.out.print("onAuthenticationSucceeded");
            if (null != mFpResultListener && null != mFpResultListener.get()) {
                mFpResultListener.get().onAuthenticateSuccess();
            }
        }

        private void notifyAuthenticationError(int errMsgId, CharSequence errString) {
            System.out.print("onAuthenticationError, errId:" + errMsgId + ", err:" + errString + ", retry after 30 seconds");
            if (null != mFpResultListener && null != mFpResultListener.get()) {
                mFpResultListener.get().onAuthenticateError(errMsgId);
            }
        }

        private void notifyAuthenticationFailed(int msgId, String errString) {
            System.out.print("onAuthenticationFailed, msdId: " +  msgId + " errString: " + errString);
            if (null != mFpResultListener && null != mFpResultListener.get()) {
                mFpResultListener.get().onAuthenticateFailed(msgId);
            }
        }

        private void prepareData() {
            if (mCancellationSignal == null) {
                mCancellationSignal = new CancellationSignal();
            }
            if (mAuthCallback == null) {
                mAuthCallback = new FingerprintManager.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errMsgId, CharSequence errString) {
                        // 多次指纹密码验证错误后，进入此方法；并且，不能短时间内调用指纹验证,一般间隔从几秒到几十秒不等
                        // 这种情况不建议重试，建议提示用户用其他的方式解锁或者认证
                        mState = NONE;
                        Log.e("finger",errMsgId+":"+errString.toString());
                        if(errMsgId==7){
                            notifyAuthenticationError(errMsgId, errString);
                            return;
                        }
                        onFailedRetry(0);
                    }

                    @Override
                    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                        mState = NONE;
                        Log.e("finger",helpMsgId+":"+helpString.toString());
                        notifyAuthenticationFailed(helpMsgId , helpString.toString());
                        onFailedRetry(helpMsgId);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        mState = NONE;
                        Log.e("finger","fail");
                        notifyAuthenticationFailed(0 , "");
                        onFailedRetry(0);
                    }

                    @Override
                    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                        mState = NONE;
                        notifyAuthenticationSucceeded();
                        Log.e("finger","success");
                    }
                };
            }
        }

        public void cancelAuthenticate() {
            if (mCancellationSignal != null && mState != CANCEL) {
                System.out.print("cancelAuthenticate...");
                mState = CANCEL;
                mCancellationSignal.cancel();
                mCancellationSignal = null;
            }
        }

        private void onFailedRetry(int msgId) {
            mFailedTimes++;
            System.out.print("on failed retry time " + mFailedTimes);
            cancelAuthenticate();
            mHandler.removeCallbacks(mFailedRetryRunnable);
            mHandler.postDelayed(mFailedRetryRunnable, 300); // 每次重试间隔一会儿再启动
        }

        private Runnable mFailedRetryRunnable = new Runnable() {
            @Override
            public void run() {
                startAuthenticate(mCryptoObjectCreator.getCryptoObject());
            }
        };

        public boolean isSupport() {
            return isSupport;
        }

        /**
         * 时候有指纹识别硬件支持
         * @return
         */
        public boolean isHardwareDetected() {
            try {
                mFingerprintManager.isHardwareDetected();
            } catch (Throwable e) {
                return false;
            }
            return true;
        }

        /**
         * 是否录入指纹，有些设备上即使录入了指纹，但是没有开启锁屏密码的话此方法还是返回false
         * @return
         */
        public boolean isHasEnrolledFingerprints() {
            try {
                // 有些厂商api23之前的版本可能没有做好兼容，这个方法内部会崩溃（redmi note2, redmi note3等）
                return mFingerprintManager.hasEnrolledFingerprints();
            } catch (Exception e) {
                return false;
            }
        }

        public static FingerprintManager getFingerprintManager(Context context) {
            FingerprintManager fingerprintManager = null;
            try {
                fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            } catch (Throwable e) {
                System.out.print("have not class FingerprintManager");
            }
            return fingerprintManager;
        }

        public void onDestroy() {
            cancelAuthenticate();
            mHandler = null;
            mAuthCallback = null;
            mFpResultListener = null;
            mCancellationSignal = null;
            mFingerprintManager = null;
            if (mCryptoObjectCreator != null) {
                mCryptoObjectCreator.onDestroy();
                mCryptoObjectCreator = null;
            }
        }
    }

    public static interface OnCancelListener {
        /**
         * This method will be invoked when the dialog is canceled.
         *
         * @param dialog The dialog that was canceled will be passed into the
         *            method.
         */
        public void onCancel(DialogInterface dialog);
    }
}
