package com.guotion.sicilia.ui.dialog;

import com.guotion.sicilia.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public abstract class AddTagDialog extends Dialog{
	private TextView titleTextView;
	private EditText tagNameEditText;
	private Button cancelButton;
	private Button sureButton;
	
	public AddTagDialog(Context context, String title) {
		super(context,R.style.Theme_CommonDialog);

		initView(title);
		initListener();
	}
	
	private void initView(String title){
		setContentView(R.layout.dialog_add_tag);
		titleTextView = (TextView)findViewById(R.id.tv_dialog_addTag_title);
		titleTextView.setText(title);
		tagNameEditText = (EditText)findViewById(R.id.editText_dialog_addTag_tagName);
		cancelButton = (Button)findViewById(R.id.button_dialog_addTag_cancel);
		sureButton = (Button)findViewById(R.id.button_dialog_addTag_sure);
	}
	
	private void initListener(){
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickCancel();
				dismiss();
			}
		});
		
		sureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = tagNameEditText.getText().toString();
				clickSure(name);
				dismiss();
			}
		});
	}
	
	public abstract void clickCancel();
	public abstract void clickSure(String name);
}