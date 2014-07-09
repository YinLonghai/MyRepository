package com.guotion.sicilia.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class AndroidSystemUtils {
	/**
	 * 关闭键盘
	 * @param activity
	 */
	public static void closeInputSoftWindow(Activity activity){
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken()
					,InputMethodManager.HIDE_NOT_ALWAYS);
		};
	}
}
