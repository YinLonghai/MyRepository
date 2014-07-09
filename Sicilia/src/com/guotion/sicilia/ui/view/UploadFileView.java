package com.guotion.sicilia.ui.view;

import com.guotion.sicilia.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class UploadFileView extends RelativeLayout {

	public UploadFileView(Context context) {
		super(context);
		initView();
	}

	public UploadFileView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	private void initView(){
		LayoutInflater.from(getContext()).inflate(R.layout.view_msg_left, this);
	}
}
