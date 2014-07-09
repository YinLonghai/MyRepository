package com.guotion.sicilia.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.guotion.common.volley.VolleyUtil;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.ConversationInfo;
import com.guotion.sicilia.bean.net.ANotification;
import com.guotion.sicilia.bean.net.Activity;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.bean.net.CloudEvent;
import com.guotion.sicilia.bean.net.Tag;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.im.util.GsonTransferUtil;
import com.guotion.sicilia.ui.listener.ImMessageToUIListener;
import com.guotion.sicilia.util.LogUtil;
import com.guotion.sicilia.util.PreferencesHelper;

//gson.fromJson(json,new TypeToken<List<ShareInfo>>(){}.getType());
public class AppData {
	public static int appState = 0;
	
	public static final String THEME = "theme";
	public static final int THEME_MALE = 0;
	public static final int THEME_RED = 1;
	public static final int THEME_BLUE = 2;
	public static final int THEME_FEMALE = 3;
	
	public static User USER = null;
	public static User OTHER_USER = null;
	
	public static VolleyUtil volleyUtil;
	public static ImMessageToUIListener imMessageToUIListener = ImMessageToUIListener.getInstance();
	
	public static final ArrayList<User> userList = new ArrayList<User>();
	public static final ArrayList<ChatGroup> chatGroupList = new ArrayList<ChatGroup>();
	public static final ArrayList<Activity> activityList = new ArrayList<Activity>();
	public static final ArrayList<ANotification> aNotificationList = new ArrayList<ANotification>();
	public static final ArrayList<CloudEvent> cloudEventList = new ArrayList<CloudEvent>();
	public static final ArrayList<Tag> tagList = new ArrayList<Tag>();
	public static final List<ConversationInfo> conversationList = new LinkedList<ConversationInfo>();
	public static final HashMap<String,List<ChatItem>> chatMap = new HashMap<String, List<ChatItem>>();
	public static final List<ChatItem> tempP2pChatList = new LinkedList<ChatItem>();
	public static final List<ChatItem> tempGroupChatList = new LinkedList<ChatItem>();
	
	public static final Map<String, String> lastReadMap = new HashMap<String, String>();
	//public static int theme = 0;
	
	public static ChatGroup tempChatGroup;
	public static ANotification tempANotification;
	public static User tempuser;
	public static CloudEvent tempCloudEvent;
	public static Activity tempActivity;
	
	/**
	 * 得到User数据
	 * @param context
	 * @return
	 */
	public static User getUser(Context context){
		if (USER == null) {
			try {
				String json = new PreferencesHelper(context).getString("user_data");	
				LogUtil.i("getuser json=" + json);
				USER = (User) GsonTransferUtil.transferToObject(json, new User());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return USER;
		
	}
	
	/**
	 * 设置User数据
	 * @param context
	 * @param user
	 */
	public static void setUser(Context context, User user, String userJson){
		USER = user;
		new PreferencesHelper(context).put("user_data", userJson);		
	}
	
	/**
	 * 退出后清空数据
	 * @param context
	 */
	public void clearUserData(Context context){
		USER = null;
		new PreferencesHelper(context).put("user_data", null);
	}
	
	public static int getThemeColor(int theme) throws SecurityException, NoSuchFieldException, NumberFormatException, IllegalArgumentException, IllegalAccessException{
		int color = 0;
		Field field;
		switch(theme){
		case THEME_MALE:
			field = R.color.class.getDeclaredField("green");
			color = Integer.parseInt(field.get(null).toString());
			break;
		case THEME_RED:
			field = R.color.class.getDeclaredField("red");
			color = Integer.parseInt(field.get(null).toString());
			break;
		case THEME_BLUE:
			field = R.color.class.getDeclaredField("blue");
			color = Integer.parseInt(field.get(null).toString());
			break;
		case THEME_FEMALE:
			field = R.color.class.getDeclaredField("orang");
			color = Integer.parseInt(field.get(null).toString());
			break;
		}
		return color;
	}
	public static int getThemeImgResId(int theme,String imgPreName) throws SecurityException, NoSuchFieldException, NumberFormatException, IllegalArgumentException, IllegalAccessException{
		int imgResId = 0;
		Field field;
		switch(theme){
		case THEME_MALE:
			field = R.drawable.class.getDeclaredField(imgPreName+"_green");
			imgResId = Integer.parseInt(field.get(null).toString());
			break;
		case THEME_RED:
			field = R.drawable.class.getDeclaredField(imgPreName+"_red");
			imgResId = Integer.parseInt(field.get(null).toString());
			break;
		case THEME_BLUE:
			field = R.drawable.class.getDeclaredField(imgPreName+"_blue");
			imgResId = Integer.parseInt(field.get(null).toString());
			break;
		case THEME_FEMALE:
			field = R.drawable.class.getDeclaredField(imgPreName+"_orang");
			imgResId = Integer.parseInt(field.get(null).toString());
			break;
		}
		return imgResId;
	}
	public static int getThemeStyleResId(int theme,String stylePreName) throws SecurityException, NoSuchFieldException, NumberFormatException, IllegalArgumentException, IllegalAccessException{
		int ResId = 0;
		Field field;
		switch(theme){
		case THEME_MALE:
			field = R.style.class.getDeclaredField(stylePreName+"_green");
			ResId = Integer.parseInt(field.get(null).toString());
			break;
		case THEME_RED:
			field = R.style.class.getDeclaredField(stylePreName+"_red");
			ResId = Integer.parseInt(field.get(null).toString());
			break;
		case THEME_BLUE:
			field = R.style.class.getDeclaredField(stylePreName+"_blue");
			ResId = Integer.parseInt(field.get(null).toString());
			break;
		case THEME_FEMALE:
			field = R.style.class.getDeclaredField(stylePreName+"_orang");
			ResId = Integer.parseInt(field.get(null).toString());
			break;
		}
		return ResId;
	}
	public static int getStringResId(String stringName){
		int ResId = 0;
		Field field;
		try {
			field = R.string.class.getDeclaredField(stringName);
			ResId = Integer.parseInt(field.get(null).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResId;
	}
	
	public static User getUser(String userId){
		for(int i=0;i<userList.size();i++){
			if(userId.equals(userList.get(i)._id)){
				return userList.get(i);
			}
		}
		return null;
	}
	public static ConversationInfo getConversation(String userIdOrGroupId){
		for(ConversationInfo conversationInfo : conversationList){
			if(userIdOrGroupId.equals(conversationInfo.accountId) || userIdOrGroupId.equals(conversationInfo.groupId)){
				return conversationInfo;
			}
		}
		return null;
	}
}
