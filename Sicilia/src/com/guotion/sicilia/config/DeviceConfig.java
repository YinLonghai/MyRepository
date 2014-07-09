package com.guotion.sicilia.config;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class DeviceConfig {
	public static float scale = 1.5f;
	public static int width = 480;
	public static void init(Context context){
		WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		width = display.getWidth();
		scale = (width)/320.0F;	
	}
}
