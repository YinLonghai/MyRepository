package com.guotion.sicilia.ui.popupwindow;

import java.util.List;

import org.apache.tools.ant.dispatch.DispatchUtils;

import com.guotion.sicilia.R;
import com.guotion.sicilia.ui.adapter.TextWheelAdapter;
import com.guotion.sicilia.ui.view.WheelView;
import com.guotion.sicilia.ui.view.WheelView.OnWheelChangedListener;
import com.guotion.sicilia.ui.view.WheelView.OnWheelScrollListener;
import com.guotion.sicilia.util.DisplayUtil;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class ChooseTextPopupwindow extends PopupWindow {
	private View popView = null;
	private WheelView textWheelView = null;	
	private List<String> textList = null;
	private OnTextChangedListener textChangedListener = null;
	public ChooseTextPopupwindow(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popView = inflater.inflate(R.layout.popupwindow_choose_text, null);
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
		textWheelView = (WheelView)popView.findViewById(R.id.wheelView_text);
		textWheelView.setCyclic(false);
		textWheelView.setVisibleItems(5);
		textWheelView.TEXT_SIZE_ITEM = DisplayUtil.sp2px(26);
		textWheelView.TEXT_SIZE_VALUE = DisplayUtil.sp2px(30);
	}
	
	private void initListener() {
//		textWheelView.addChangingListener(new OnWheelChangedListener() {
//			
//			@Override
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				if(textChangedListener != null){
//					textChangedListener.textChanged(newValue, textList.get(newValue));
//				}
//			}
//		});
		textWheelView.addScrollingListener(new OnWheelScrollListener() {
			
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScrollingFinished(WheelView wheel) {
				if(textChangedListener != null){
					textChangedListener.textChanged(wheel.getCurrentItem(), textList.get(wheel.getCurrentItem()));
				}
				
			}
		});
	}
	
	public void setTextList(List<String> list){
		this.textList = list;
		textWheelView.setAdapter(new TextWheelAdapter(list));
	}
	
	public void setOnTextChangedListener(OnTextChangedListener l){
		this.textChangedListener = l;
	}		
	
	public interface OnTextChangedListener{
		public void textChanged(int index, String text);
	}
}
