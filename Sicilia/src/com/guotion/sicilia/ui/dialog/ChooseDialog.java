package com.guotion.sicilia.ui.dialog;

import com.guotion.sicilia.R;
import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

public class ChooseDialog extends Dialog{
	
	private TextView tvChoose;
	private Button btnSure;
	private ChooseClickListener chooseClickListener;

	public ChooseDialog(Context context) {
		super(context,R.style.Theme_CommonDialog);
		
		initView(context);
		initListener();
	}
	
	private void initView(Context context){
		setContentView(R.layout.dialog_choose);
		tvChoose = (TextView)findViewById(R.id.TextView_choose);
		btnSure = (Button)findViewById(R.id.button_dialog_input_sure);
	}
	
	public interface ChooseClickListener{
		public void sureClick();
	}
	
	public void setOnChooseClickListener(ChooseClickListener l){
		this.chooseClickListener = l;
	}
	
	public void setText(String string){
		tvChoose.setText(string);
	}
	
	public void setText(int string){
		tvChoose.setText(string);
	}
	
	private void initListener(){
		btnSure.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (chooseClickListener != null) {
					chooseClickListener.sureClick();
				}
				dismiss();
			}
		});
	}
}