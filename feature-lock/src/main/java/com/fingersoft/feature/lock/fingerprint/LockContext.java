package com.fingersoft.feature.lock.fingerprint;

import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import com.fingersoft.feature.lock.LockManager;
import org.jetbrains.annotations.NotNull;

public class LockContext {
    private static FingerprintManagerCompat fingerprintManager;
    private static Context application;
    public static void init(Context app) {
        application = app;
        fingerprintManager = FingerprintManagerCompat.from(application);
    }

    /**
     * 硬件是否支持
     * @return
     */
    public static boolean isHardwareSupportFingerprint() {
        return fingerprintManager != null && (fingerprintManager.isHardwareDetected());
    }

    /**
     * 当前设备必须是处于安全保护中的。android特有，有屏幕锁才可以使用指纹解锁
     * @return
     */
    public static boolean isKeyguardSecure() {
        KeyguardManager keyguardManager = (KeyguardManager) application.getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager.isKeyguardSecure();
    }

    /**
     * 是否有注册指纹
     * @return
     */
    public static boolean hasEnrolledFingerprints() {
        return fingerprintManager != null && (fingerprintManager.hasEnrolledFingerprints());
    }
}
