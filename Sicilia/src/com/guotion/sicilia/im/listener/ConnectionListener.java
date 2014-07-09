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
}
