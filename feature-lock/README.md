# fecture-lock-new模块
[TOC]

### 简介
>lock模块包含密码登录，手势密码，指纹登陆三个小模块。手势密码模块包含设置密码跟解锁两部分，指纹是指纹验证功能。
### 使用方式
>1.在主工程的setting.gradle中添加模块引用 ':feature-lock-new'

      include ':app', ':feature-lock-new'
>2.在需要使用的模块中的build.gradle中的dependencies标签下添加
>compile project(path: ':feature-lock-new')

	dependencies {
      compile 'com.lzy.net:okgo:2.1.4'
      compile "org.jetbrains.kotlin:kotlin-stdlib-jre7:$kotlin_version"
      compile project(path: ':feature-lock-new')
      }


###  ReLogin
>直接使用密码登陆，下方可通过更多切换登录方式。（方法传递用户名，登陆页面输入密码后进行登陆）

##### doReLoginLock：密码登陆
 doReLoginLock(context: Context, userName: String, bitmap: Bitmap?, listener: LockListener)

>参数
>@context ： 上下文
>@userName ： 用户名
>@bitmap ： 用户头像(传null的话是默认头像)
>@listener ： 返回结果LockListener

使用实例:

	  LockManager.instance.doReLoginLock(v.context, "wangxf", null,this)

* LockListener中关于Relogin的实现方法:

	  //登陆成功
	  fun onLoginSuccess(success: String);
	  //登陆失败
	  fun onLoginFail(fail: String);


###  GestureLock
>手势解锁，包含手势密码的设置，验证，清除，重置功能。

##### doResetGesturePassword：设置与重置手势密码:
 doResetGesturePassword(context: Context,bitmap:Bitmap?, listener: LockListener)

>参数
>@context ： 上下文
>@bitmap ： 用户头像(传null的话是默认头像)
>@listener ： 返回结果LockListener

使用实例:

	LockManager.instance.doResetGesturePassword(v.context,null, this)

* LockListener中关于doResetGesturePassword的实现方法:

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



##### doGestureLock：验证手势密码

  doGestureLock(context: Context,bitmap:Bitmap?, listener: LockListener)
  本方法与doResetGesturePassword内部调用api为同一个，只不过使用场景不同

>参数
>@context ： 上下文
>@bitmap ： 用户头像(传null的话是默认头像)
>@listener ： 返回结果LockListener

使用实例:

	LockManager.instance.doGestureLock(v.context, null,this)

* LockListener中关于GestureLock的实现方法:

	//尝试密码次数超限
	fun onGustureRetryLimitCallBack();
    //密码认证成功
    fun onGustureMatchSuccess();
    //密码认证失败
    fun onGustureMatchError();

###  FingerprintLock
>指纹解锁，包含指纹验证功能
##### doFingerprintLock：验证指纹
 doFingerprintLock(context: Context,bitmap:Bitmap?, listener: LockListener)

>参数
>@context ： 上下文
>@bitmap ： 用户头像(传null的话是默认头像)
>@listener ： 返回结果LockListener

使用实例:

	LockManager.instance.doResetGesturePassword(v.context,null, this)

* LockListener中关于GestureLock的实现方法:

	//指纹验证失败
    fun onFingerPrintMatchFail();
    //指纹验证成功
    fun onFingerPrintMatchSuccess();
    //指纹验证错误次数过多
    fun onFingerPrintMatchError();


###注
1.此版为模块化初版，入口统一为api形式，并且模块内不会耦合与模块无关的api，此模块可以单独引用并且单独测试，如果以后需要用到额外的参数传递或者页面上的改动请联系作者。
2.免用户名登陆小模块中现在只传递了用户名作为登陆凭证，密码从模块中获取，如果登陆接口需要更多的信息，可以考录在本模块中添加，也可以从免用户名登陆模块中返回密码，自行实现登陆请求。
3.关于用户头像，本模块默认用户登录后应该已经获取了头像，直接传递Bitmap对象到api即可。



