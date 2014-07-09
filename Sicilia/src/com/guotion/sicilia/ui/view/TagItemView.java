package com.guotion.sicilia.ui.view;

import com.guotion.sicilia.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TagItemView extends RelativeLayout{
	private TextView name;

	public TagItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public TagItemView(Context context) {
		super(context);
		initView();
	}
	private void initView(){
		LayoutInflater.from(getContext()).inflate(R.layout.listview_item_tag, this);
		name = (TextView) findViewById(R.id.textView_name);
	}
	public void setData(String data){
		name.setText(data);
	}
}
