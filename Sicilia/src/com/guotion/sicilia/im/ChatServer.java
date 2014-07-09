package com.guotion.sicilia.im;

import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.im.util.X509TrustManagerImpl;
import io.socket.IOCallback;
import io.socket.SocketIO;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.net.MalformedURLException;
import java.security.SecureRandom;

/**
 * Created with IntelliJ IDEA.
 * User: lizhengbin
 * Date: 14-4-15
 * Time: 下午1:02
 * To change this template use File | Settings | File Templates.
 */
public class ChatServer {
    private static ChatServer instance = new ChatServer();
    private SocketIO socketIO = null;
    private Chat chat;
    private String userId;
    private IOCallback ioCallback;

    private ChatServer() {
    }

    public static ChatServer getInstance() {
        return instance;
    }

    /**
     * @param iocallback 消息通知接口
     * @throws Exception
     */
    public void login(String userId, IOCallback iocallback) throws Exception {
        this.userId = userId;
        if (iocallback == null)
            throw new Exception("iocallback不能为空");
        this.ioCallback = iocallback;
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(new KeyManager[0], new X509TrustManager[]{new X509TrustManagerImpl()}, new SecureRandom());
        SocketIO.setDefaultSSLSocketFactory(context);
        try {
            socketIO = new SocketIO(ChatServerConstant.URL.SERVER_HOST);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //ignore ,不会发生这种情况
        }
        socketIO.connect(iocallback);
        chat = new Chat(socketIO, userId);
    }

    /**
     * logout the chatserver.
     */
    public void logout() {
        if (socketIO != null) {
            socketIO.disconnect();
            socketIO = null;
        }
    }

    public Chat getChat() {
        return chat;
    }

    public synchronized void relogin() throws Exception {
        this.logout();
        System.out.println("iocallback="+ioCallback);
        this.login(userId, ioCallback);
    }
}
