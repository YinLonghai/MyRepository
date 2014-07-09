package com.guotion.sicilia.ui.dialog;

import com.guotion.sicilia.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public abstract class WarningDialog extends Dialog{
	private TextView warningTextView;
	private Button cancelButton;
	private Button sureButton;
	
	public WarningDialog(Context context, String warning, String cancel, String sure) {
		super(context,R.style.Theme_CommonDialog);

		initView(warning, cancel, sure);
		initListener();
	}
	
	private void initView(String warning, String cancel, String sure){
		setContentView(R.layout.dialog_warning);
		warningTextView = (TextView)findViewById(R.id.tv_dialog_warning);
		warningTextView.setText(warning);
		cancelButton = (Button)findViewById(R.id.button_dialog_warning_cancel);
		cancelButton.setText(cancel);
		sureButton = (Button)findViewById(R.id.button_dialog_warning_sure);
		sureButton.setText(sure);
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
				clickSure();
				dismiss();
			}
		});
	}
	
	public abstract void clickCancel();
	public abstract void clickSure();
}