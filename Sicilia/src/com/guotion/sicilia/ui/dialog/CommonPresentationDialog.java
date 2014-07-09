package com.guotion.sicilia.ui.dialog;

import com.guotion.sicilia.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CommonPresentationDialog extends Dialog{
	private TextView presentationTextView;
	private Button sureButton;
	
	public CommonPresentationDialog(Context context, String presentation) {
		super(context,R.style.Theme_CommonDialog);
		initView(presentation);
		initListener();
	}

	private void initView(String presentation) {
		setContentView(R.layout.dialog_commom_presentation);
		presentationTextView = (TextView)findViewById(R.id.tv_dialog_common_presentation);
		presentationTextView.setText(presentation);
		sureButton = (Button)findViewById(R.id.button_dialog_common_presentation_sure);
	}

	private void initListener() {
		sureButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
	
}