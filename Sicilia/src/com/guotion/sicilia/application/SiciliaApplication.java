package com.guotion.sicilia.application;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class SiciliaApplication extends Application{

	private List<Activity> list;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		System.out.println("SiciliaApplication onCreate");
		list = new LinkedList<Activity>();
		super.onCreate();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		System.out.println("onLowMemory");
		clear();
		super.onLowMemory();
	}

	public void addActivity(Activity activity){
		list.add(activity);
	}
	
	public void removeActivity(Activity activity){
		
	}
	
	public void clear(){
		for(Activity activity : list){
			if(activity != null && !activity.isFinishing()){
				activity.finish();
			}
		}
	}
}
