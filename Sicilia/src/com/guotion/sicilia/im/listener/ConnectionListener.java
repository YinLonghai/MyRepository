package com.guotion.sicilia.im.listener;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-25
 * Time: 上午10:15
 * To change this template use File | Settings | File Templates.
 */
public interface ConnectionListener {

    public void notifyConnectionEstablished();

    public void notifyAnErrorOccur(Exception e);

    public void notifyConnectionClosed();
    
    /**
     * 其他的设备登录该账号，此时过三秒后回到登录界面
     */
    public void notifyOtherDiviceLogin();
}
