package com.guotion.sicilia.ui.dialog;

import com.guotion.sicilia.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public abstract class InputGroupNameDialog extends Dialog{
	private EditText groupName;
	private Button sure;

	public InputGroupNameDialog(Context context) {
		super(context,R.style.Theme_CommonDialog);
		
		initView();
		initListener();
	}
	
	public abstract void clickSure(String groupName);
	
	private void initView(){
		setContentView(R.layout.dialog_input_groupname);
		groupName = (EditText)findViewById(R.id.edt_dialog_input_groupName);
		sure = (Button)findViewById(R.id.button_dialog_input_sure);
	}
	
	private void initListener(){
		sure.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = groupName.getText().toString();
				dismiss();
				clickSure(name);
			}
		});
	}
}