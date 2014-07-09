package com.guotion.sicilia.ui.dialog;

import com.guotion.sicilia.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public abstract class ActivityInviteDialog extends Dialog{
	private Button join;
	private Button laterResponse;
	private Button decline;

	public abstract void clickJoin();
	public abstract void clickLaterResponse();
	public abstract void clickDecline();

	public ActivityInviteDialog(Context context) {
		super(context,R.style.Theme_CommonDialog);
		
		initView();
		initListener();
	}
	
	private void initView(){
		setContentView(R.layout.dialog_activity_invite);
		join = (Button)findViewById(R.id.button_dialog_activity_invite_join);
		laterResponse = (Button)findViewById(R.id.button_dialog_activity_invite_laterResponse);
		decline = (Button)findViewById(R.id.button_dialog_activity_invite_decline);
	}
	
	private void initListener(){
		join.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				clickJoin();
			}
		});
		
		laterResponse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				clickLaterResponse();
			}
		});
		
		decline.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				clickDecline();
			}
		});
	}
}