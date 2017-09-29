# fecture-lock模块


#####简介：
>lock模块包含密码登录，手势密码，指纹登陆三个小模块。手势密码模块包含设置密码跟解锁两部分，指纹是指纹验证功能。
         

###  ReLogin模块
>直接使用密码登陆，下方可通过更多切换登录方式。（方法传递用户名，登陆页面输入密码后进行登陆）

###### API （密码登陆）: doReLoginLock(context: Context, userName: String, bitmap: Bitmap?, listener: LockListener)   

>参数       
>@context ： 上下文       
>@userName ： 用户名             
>@bitmap ： 用户头像(传null的话是默认头像)
>@listener ： 返回结果LockListener

使用实例:             

	     LockManager.instance.doReLoginLock(v.context, "wangxf", null,this)
     
LockListener关于Relogin的实现方法为
>    fun onLoginSuccess(success: String);//登陆成功
> fun onLoginFail(fail: String);    //登陆失败


###  GestureLock模块
>手势解锁，包含手势密码的设置，验证，清除，重置功能。

###### API （设置与重置手势密码）:  doResetGesturePassword(context: Context,bitmap:Bitmap?, listener: LockListener)     

>参数
>@context ： 上下文
>@userName ： 用户名
>@bitmap ： 用户头像(传null的话是默认头像)
>@listener ： 返回结果LockListener

使用实例:

	      LockManager.instance.doResetGesturePassword(v.context,null, this)

#### 注：
>vpn的注册需要跟用户交互，产生用户确认页面，所以在activity中进行，并且注册的过程是异步的，所以如果app需要启动后立刻使用vpn发起请求，一定要在OnVPNInitCallback回调成功后再进行后续操作，否则注册完成前进行的请求不会经过vpn。

