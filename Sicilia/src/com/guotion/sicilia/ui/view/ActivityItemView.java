package com.guotion.sicilia.ui.view;

import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.ActivityInfo;
import com.guotion.sicilia.bean.net.Activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityItemView extends RelativeLayout{
	private TextView name;
	private TextView content;
	private TextView date;
	
	private Activity activityInfo;

	public ActivityItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public ActivityItemView(Context context) {
		super(context);
		initView();
	}
	private void initView(){
		LayoutInflater.from(getContext()).inflate(R.layout.listview_item_activity, this);
		name = (TextView) findViewById(R.id.textView_name);
		content = (TextView) findViewById(R.id.textView_content);
		date = (TextView) findViewById(R.id.textView_time);
	}
	
	public void setData(Activity activityInfo){
		this.activityInfo = activityInfo;
		initData();
	}
	private void initData(){
		name.setText(activityInfo.getName());
		content.setText(activityInfo.getContent());
		date.setText(activityInfo.getDate());
	}
}
