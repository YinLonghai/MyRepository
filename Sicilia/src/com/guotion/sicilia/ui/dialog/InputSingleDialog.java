package com.guotion.sicilia.ui.dialog;

import com.guotion.sicilia.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public abstract class InputSingleDialog extends Dialog{
	private EditText etContent;
	private Button btnsSure;

	public InputSingleDialog(Context context) {
		super(context,R.style.Theme_CommonDialog);
		
		initView();
		initListener();
	}
	
	public abstract void clickSure(String groupName);
	
	private void initView(){
		setContentView(R.layout.dialog_input_single);
		etContent = (EditText)findViewById(R.id.edt_dialog_input);
		btnsSure = (Button)findViewById(R.id.button_dialog_input_sure);
	}
	
	private void initListener(){
		btnsSure.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = etContent.getText().toString();
				dismiss();
				clickSure(name);
			}
		});
	}
}