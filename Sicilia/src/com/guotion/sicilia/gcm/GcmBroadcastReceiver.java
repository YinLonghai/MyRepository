package com.guotion.sicilia.gcm;

import org.json.JSONException;
import org.json.JSONObject;

import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.ui.dialog.NoticeChangeDialog;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
	NoticeChangeDialog noticeChangeDialog;
	private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
    	this.context = context;
    	Bundle extras = intent.getExtras();
    	Log.i("GCMDemo","接收到的推送内容是："+extras.toString());
    	if (!extras.isEmpty()) {
    		try {
    			JSONObject json = new JSONObject(extras.toString());
    			JSONObject apsJSON = new JSONObject(json.getString("aps"));
    			//提示的内容
    			String content = apsJSON.getString("alert");
    			//"type" : "bar",//Birthday,Authorized,System,Activity,ChatItem
    			//现在system，Authorized类型的没处理，只有chatitem和活动的才用处理
    			String type = json.getString("type");
    			String id = json.getString("_id");
    			handle(type,content,id);
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
    	}
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
//    Birthday,
//    Authorized,审核通知
//    System,系统通知
//    Activity,活动通知
//    ChatItem,对话信息
//    Register,注册申请
//    Board 公告
    private void handle(String type,String content,String id){
    	if(AppData.appState == 0) return ;
    	if(type.equals("Board")){
    		showDialog("公告",content);
    	}else if(type.equals("System")){
    		showDialog("系统通知",content);
    	}else if(type.equals("ChatItem")){
    		
    	}
    }
    private void showDialog(String type,String content){
    	if(noticeChangeDialog == null){
			noticeChangeDialog = new NoticeChangeDialog(context,"",content) {
				@Override
				public void sureListener() {
					// TODO Auto-generated method stub
				}
			};
		}
		noticeChangeDialog.setTitle(type);
		noticeChangeDialog.setContent(content);
		noticeChangeDialog.show();
    }
}