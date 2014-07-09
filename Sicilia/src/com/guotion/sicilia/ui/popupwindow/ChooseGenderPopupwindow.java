package com.guotion.sicilia.ui.popupwindow;

import com.guotion.sicilia.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

public class ChooseGenderPopupwindow extends PopupWindow {
	private static ChooseGenderPopupwindow mInstance = null;
	private View popView;
	private Button femaleButton;
	private Button maleButton;
	private Button cancelButton;
	
	GetGenderListener getGenderListener;

	public ChooseGenderPopupwindow(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popView = inflater.inflate(R.layout.popupwindow_choose_gender, null);
		mInstance = this;
		initView();
		initListener();
	}

	private void initView() {
		// 让泡泡窗口获取焦点
		super.setFocusable(true);
		// 点击其它地方收起泡泡窗口
		super.setBackgroundDrawable(new BitmapDrawable());
		// 设置弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置PopupWindow的View
		this.setContentView(popView);
		// 设置弹出窗体动画效果
		// this.setAnimationStyle(R.style.AnimBottom);
		femaleButton = (Button)popView.findViewById(R.id.button_popup_choose_gender_female);
		maleButton = (Button)popView.findViewById(R.id.button_popup_choose_gender_male);
		cancelButton = (Button)popView.findViewById(R.id.button_popup_choose_gender_cancel);
	}

	private void initListener() {
		femaleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getGenderListener.getGender(femaleButton.getText().toString());
				if(mInstance != null && mInstance.isShowing())
					dismiss();
			}
		});
		
		maleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getGenderListener.getGender(maleButton.getText().toString());
				if(mInstance != null && mInstance.isShowing())
					dismiss();
			}
		});
		
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mInstance != null && mInstance.isShowing())
					dismiss();
			}
		});
	}
	
	public void setGenderListener(GetGenderListener getGenderListener){
		this.getGenderListener = getGenderListener;
	}
	
	public interface GetGenderListener{
		public void getGender(String string);
	}
}