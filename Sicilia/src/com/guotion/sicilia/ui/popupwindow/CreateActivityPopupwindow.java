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

public class CreateActivityPopupwindow extends PopupWindow {
//	private static CreateActivityPopupwindow mInstance = null;
	private Context context;
	private View popView;
	private Button notJoinButton;
	private Button joinButton;
	private Button cancelCreateButton;
	
	private OnJoinListener onJoin;
	private boolean isCancel = true;
	public CreateActivityPopupwindow(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popView = inflater.inflate(R.layout.popupwindow_create_activity, null);
//		mInstance = this;
		this.context = context;
		initView();
		initListener();
	}
	
	public boolean isCancel(){
		return isCancel;
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
		notJoinButton = (Button)popView.findViewById(R.id.button_popup_create_activity_notJoin);
		joinButton = (Button)popView.findViewById(R.id.button_popup_create_activity_join);
		cancelCreateButton = (Button)popView.findViewById(R.id.button_popup_create_activity_cancelCreate);
	}
	
	private void initListener(){
		notJoinButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isCancel = false;
				dismiss();
				onJoin.notJoinActivity();
			}
		});
		
		joinButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isCancel = false;
				dismiss();
				onJoin.joinActivity();
			}
		});
		
		cancelCreateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
	
	public interface OnJoinListener{
		public void notJoinActivity();
		public void joinActivity();
	}
	
	@Override
	public void showAsDropDown(View anchor) {
		isCancel = true;
		super.showAsDropDown(anchor);
	}

	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff) {
		isCancel = true;
		super.showAsDropDown(anchor, xoff, yoff);
	}

	
	
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		isCancel = true;
		super.showAtLocation(parent, gravity, x, y);
	}

	public void setOnJoinListener(OnJoinListener onJoinListener){
		this.onJoin = onJoinListener;
	}
}