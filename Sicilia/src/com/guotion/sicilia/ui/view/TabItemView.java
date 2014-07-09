package com.guotion.sicilia.ui.view;


import com.guotion.sicilia.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TabItemView extends RelativeLayout {
	
	private TextView tvNewInfo;
	private ImageView ivIcon;
	private int[] icons = null;
	private boolean isSelected = false;
	public TabItemView(Context context){
		super(context);
		initView(context);
		initListener();
	}
	public TabItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		initListener();
		
	}
	private void initView(Context context) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.layout_main_tabtitle_item, this, true);
		tvNewInfo = (TextView) view.findViewById(R.id.TextView_newinfor);
		ivIcon = (ImageView) view.findViewById(R.id.ImageView_tab_icon);
	}
	private void initListener() {
		// TODO Auto-generated method stub
		
	}

	public void setTitle(String title){
		if (title == null || title.equals("")) {
			tvNewInfo.setVisibility(View.GONE);
		} else {
			tvNewInfo.setVisibility(View.VISIBLE);
		}
		tvNewInfo.setText(title);	
	}
	
	public void setTitle(int resId){
		if (resId == 0) {
			tvNewInfo.setVisibility(View.GONE);
		} else {
			tvNewInfo.setVisibility(View.VISIBLE);
		}
		tvNewInfo.setText(resId+"");			
	}
	
	public void setIcon(int[] icons){
		if (!isSelected) {
			ivIcon.setImageResource(icons[0]);			
		}else {
			ivIcon.setImageResource(icons[1]);
		}
		this.icons = icons;
	}
	public void isSelected(boolean isSelected){
		this.isSelected = isSelected;
		if (isSelected) {
			if (icons != null) {
				ivIcon.setImageResource(icons[1]);
			}
		} else {
			if (icons != null) {
				ivIcon.setImageResource(icons[0]);
			}
		}
	}
}
