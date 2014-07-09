package com.guotion.sicilia.ui.dialog;

import com.guotion.sicilia.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public abstract class NoticeChangeDialog extends Dialog{
	private TextView titleTextView;
	private TextView contentTextView;
	private Button sureButton;
	
	public abstract void sureListener();

	public NoticeChangeDialog(Context context, String title, String content) {
		super(context);
		initView(title, content);
		initListener();
	}

	private void initView(String title, String content) {
		setContentView(R.layout.dialog_notice_change);
		titleTextView = (TextView)findViewById(R.id.tv_dialog_notice_change_title);
		titleTextView.setText(title);
		contentTextView = (TextView)findViewById(R.id.tv_dialog_notice_change_content);
		contentTextView.setText(content);
		sureButton = (Button)findViewById(R.id.button_dialog_notice_change_ok);
	}

	private void initListener() {
		sureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sureListener();
			}
		});
	}
	
	public void setTitle(String title){
		titleTextView.setText(title);
	}
	
	public void setContent(String content){
		contentTextView.setText(content);
	}
}