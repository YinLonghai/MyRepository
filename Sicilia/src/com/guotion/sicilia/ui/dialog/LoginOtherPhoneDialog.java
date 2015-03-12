package com.guotion.sicilia.ui.dialog;

import com.guotion.sicilia.R;

import android.app.Dialog;
import android.content.Context;

public class LoginOtherPhoneDialog extends Dialog{

	public LoginOtherPhoneDialog(Context context) {
		super(context, R.style.Theme_CommonDialog);
		
		initView();
	}

	private void initView() {
		setContentView(R.layout.dialog_logout);
	}
}