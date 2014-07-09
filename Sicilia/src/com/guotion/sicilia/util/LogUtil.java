package com.guotion.sicilia.util;

import android.util.Log;

public class LogUtil {
	public static void i(String msg){
		Log.i("sicilia info:", msg);
	}
	
	public static void d(String msg){
		Log.d("sicilia debug:", msg);
	}
	
	public static void e(String msg){
		Log.e("sicilia error:", msg);
	}
	
	public static void w(String msg){
		Log.w("sicilia warn:", msg);
	}
}
