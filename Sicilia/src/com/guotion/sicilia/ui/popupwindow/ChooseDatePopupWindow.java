package com.guotion.sicilia.ui.popupwindow;

import java.util.List;

import com.guotion.sicilia.R;
import com.guotion.sicilia.ui.listener.OnDateChooseListener;
import com.guotion.sicilia.ui.view.DateWheelView;
import com.guotion.sicilia.ui.view.DateWheelView.OnDateChangedListener;
import com.guotion.sicilia.util.LunarCalendar;
import com.guotion.sicilia.util.StringUtils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ChooseDatePopupWindow extends PopupWindow {
	private View popView = null;
	private DateWheelView dateWheelView = null;	
	private OnDateChooseListener dateChooseListener = null;
	private TextView tvLunarCalenar = null;
	private LunarCalendar lunarCalendar = null;
	public ChooseDatePopupWindow(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popView = inflater.inflate(R.layout.popupwindow_choose_date, null);
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
		dateWheelView = (DateWheelView)popView.findViewById(R.id.dateWheelView_choose_date);
		tvLunarCalenar = (TextView)popView.findViewById(R.id.textView_choose_lunar_date);
		lunarCalendar = new LunarCalendar();
		tvLunarCalenar.setText(lunarCalendar.toString());
	}

	private void initListener() {
		dateWheelView.setOnDateChangedListener(new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DateWheelView v, int year, int month, int day) {
				LunarCalendar lunarCalendar = new LunarCalendar(year, month, day);
				tvLunarCalenar.setText(lunarCalendar.toString());
				if (dateChooseListener != null) {
					dateChooseListener.onDateChoosed(year, month, day);
				}
			}
		});
	}
	
	
	
	@Override
	public void dismiss() {
		if (dateChooseListener != null) {
			dateChooseListener.onDateChoosed(dateWheelView.getYear(), dateWheelView.getMonth(), dateWheelView.getDay());
		}
		super.dismiss();
	}

	public void setOnDateChooseListener(OnDateChooseListener l){
		this.dateChooseListener = l;
	}

	public void setDate(String date) {
		List<Integer> dates = StringUtils.getIntegers(date, "-");
		LunarCalendar lunarCalendar = new LunarCalendar(dates.get(0), dates.get(1), dates.get(2));
		tvLunarCalenar.setText(lunarCalendar.toString());
		dateWheelView.setYear(dates.get(0));
		dateWheelView.setMonth(dates.get(1));
		dateWheelView.setDay(dates.get(2));
	}
}