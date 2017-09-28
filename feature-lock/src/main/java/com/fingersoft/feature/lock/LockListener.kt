package com.fingersoft.feature.lock.view.listener

 interface LockListener {

    //尝试密码次数超限
    fun onGustureRetryLimitCallBack();

    //密码认证成功
    fun onGustureMatchSuccess();

    //密码认证失败
    fun onGustureMatchError();

    //设置密码成功
    fun onGustureSetSuccess()

    //密码删除成功
    fun onGustureRemoveCallBack()

    //登陆成功
    fun onLoginSuccess(success: String);
    //登陆失败
    fun onLoginFail(fail: String);
}