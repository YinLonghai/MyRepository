package com.guotion.sicilia.im.listener;

import com.guotion.sicilia.bean.net.ChatItem;

/**
 * Created with IntelliJ IDEA.
 * User: lizhengbin
 * Date: 14-4-25
 * Time: 上午10:04
 * To change this template use File | Settings | File Templates.
 */
public interface MessageListener {

    /**
     * 收到群消息
     * @param chatItem
     * @param p2pGroupId 单对单聊天组的id
     * @param senderId   发送消息者的id
     */
    public void notifyReceiveP2PMessage(ChatItem chatItem, String p2pGroupId, String senderId);

    /**
     * 收到私密消息
     * @param chatItem
     * @param groupId      单对单聊天组的id
     * @param senderId  发送消息者的id
     */
    public void notifyReceiveGroupMessage(ChatItem chatItem, String groupId, String senderId);

    /**
     * 消息送达
     * @param chatItem
     */
    public void notifyMessageSendSuccess(ChatItem chatItem);

    /**
     * 消息已读
     * @param chatItem
     */
    public void notifyMessageReaded(ChatItem chatItem);
    /**
     *
     */
    public void notifyReceiveSystemMessage();





}





