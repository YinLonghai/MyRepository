package com.guotion.sicilia.ui.dialog;

import com.guotion.sicilia.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public abstract class ActivityNoticeDialog extends Dialog {
	private TextView notice;
	private Button know;
	private Button check;
	
	public abstract void clickIKnow();
	public abstract void clickCheck();

	public ActivityNoticeDialog(Context context) {
		super(context,R.style.Theme_CommonDialog);

		initView();
		initListener();
	}
	
	public void setNotice(String string){
		notice.setText(string);
	}

	private void initView() {
		setContentView(R.layout.dialog_activity_notice);
		notice = (TextView) findViewById(R.id.tv_dialog_notice);
		know = (Button) findViewById(R.id.button_dialog_notice_iKnow);
		check = (Button) findViewById(R.id.button_dialog_notice_check);
	}

	private void initListener() {
		know.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				clickIKnow();
			}
		});
		
		check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				clickCheck();
			}
		});
	}
}