package com.guotion.common.PictureBrowser;

import java.util.ArrayList;



import com.guotion.sicilia.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PagePointView extends LinearLayout {

	private int position = 0;
	private int totalNum = 0;
	private ArrayList<ImageView> ivPoints = new ArrayList<ImageView>();
	private LinearLayout llPagePoint = null;
	public PagePointView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.view_page_point, this, true);
		llPagePoint = (LinearLayout)findViewById(R.id.linearLayout_page_point);
	}

	public PagePointView(Context context) {
		super(context);
		initView(context);
	}
	
	public void setPosition(int position){
		ivPoints.get(this.position).setImageResource(R.drawable.ic_point_normal);
		ivPoints.get(position).setImageResource(R.drawable.ic_point_press);
		this.position = position;		
	}
	
	public void addTotalNum(){
		LayoutParams params = new LayoutParams(UiUtil.dip2px(15f), UiUtil.dip2px(15f));
		ImageView imageView = new ImageView(getContext());
		imageView.setImageResource(R.drawable.ic_point_normal);
		imageView.setPadding(UiUtil.dip2px(2f), UiUtil.dip2px(2f), UiUtil.dip2px(2f), UiUtil.dip2px(2f));
		imageView.setLayoutParams(params);
		llPagePoint.addView(imageView);
		ivPoints.add(imageView);
	}
	
	public void clear(){
		ivPoints.clear();
		llPagePoint.removeAllViews();
		this.position = 0;
	}
	
	
	public void setTotalNum(int totalNum){
		clear();
		this.totalNum = totalNum;
		ImageView imageView;
		LayoutParams params = new LayoutParams(UiUtil.dip2px(10), UiUtil.dip2px(10));
		for(int i = 0; i< totalNum; i++){
			imageView = new ImageView(getContext());
			imageView.setImageResource(R.drawable.ic_point_normal);
			imageView.setPadding(UiUtil.dip2px(1.5F), UiUtil.dip2px(1.5F), UiUtil.dip2px(1.5F), UiUtil.dip2px(1.5F));
			imageView.setLayoutParams(params);
			llPagePoint.addView(imageView);
			ivPoints.add(imageView);
		}
		ivPoints.get(0).setImageResource(R.drawable.ic_point_press);
		
	}

	public int getPosition() {
		return position;
	}

	public int getTotalNum() {
		return totalNum;
	}
	
	
	
}
