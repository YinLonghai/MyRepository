package com.guotion.sicilia.util;

import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.constants.AndroidRequestCode;
import com.guotion.sicilia.ui.ActivityInfoActivity;
import com.guotion.sicilia.ui.BirthdayRemindItemActivity;
import com.guotion.sicilia.ui.BirthdayRemindTimeActivity;
import com.guotion.sicilia.ui.RegisterManageActivity;
import com.guotion.sicilia.ui.UserCloudsActivity;
import com.guotion.sicilia.ui.UserInfoActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class UISkip {
	private static Intent intent = null;
	/**
	 * 界面跳转
	 * @param isClearTop 是否清除顶部
	 * @param from
	 * @param to
	 */
	public static void skip(boolean isClearTop, Context from,  Class<?> to){
		intent = new Intent(from, to);
		if (isClearTop) {
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
		from.startActivity(intent);
	}
	
	public static void skipToUserInfoActivity(Activity activity){
		intent = new Intent(activity, UserInfoActivity.class);
		activity.startActivityForResult(intent, AndroidRequestCode.REQ_CODE_EDIT_USER_INFO);
	}
	
	public static void skipToActivityInfoActivity(Context from,String activityId){
		intent = new Intent(from, ActivityInfoActivity.class);
		intent.putExtra("activityId", activityId);
		from.startActivity(intent);
	}
	public static void skipToBirthdayRemindTimeActivity(Context from,String activityId,String birthday){
		intent = new Intent(from, BirthdayRemindTimeActivity.class);
		intent.putExtra("activityId", activityId);
		intent.putExtra("birthday", birthday);
		from.startActivity(intent);
	}
	
	public static void skipToBirthdayRemindItemActivity(Context from,User user,String activityId){
		intent = new Intent(from, BirthdayRemindItemActivity.class);
		intent.putExtra("user", user);
		intent.putExtra("activityId", activityId);
		from.startActivity(intent);
	}
	
	public static void skipToUserCloudsActivity(Context from,User user){
		intent = new Intent(from, UserCloudsActivity.class);
		intent.putExtra("user", user);
		from.startActivity(intent);
	}
//	public static void skipToCloudActivity(Context from,String cloudId){
//		intent = new Intent(from, CloudActivity.class);
//		intent.putExtra("cloudId", cloudId);
//		from.startActivity(intent);
//	}
	public static void skipToRegisterManageActivity(Context from,String id,String headPhoto,String userName,String passWord,String birthday,String level,String authorized,String accountState){
		intent = new Intent(from, RegisterManageActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("headPhoto", headPhoto);
		intent.putExtra("userName", userName);
		intent.putExtra("passWord", passWord);
		intent.putExtra("birthday", birthday);
		intent.putExtra("level", level);
		intent.putExtra("authorized", authorized);
		intent.putExtra("accountState", accountState);
		from.startActivity(intent);
	}
}
