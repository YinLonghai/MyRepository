package com.guotion.sicilia.ui.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.guotion.sicilia.R;
import com.guotion.sicilia.ui.adapter.NumericWheelAdapter;
import com.guotion.sicilia.ui.adapter.TextWheelAdapter;
import com.guotion.sicilia.ui.view.WheelView.OnWheelChangedListener;
import com.guotion.sicilia.util.DisplayUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class TimeWheelView extends LinearLayout implements OnWheelChangedListener {

	private WheelView wvHalfDay;
	private WheelView wvHour;
	private WheelView wvMinute;
	private Calendar mCalendar;
	private int halfDay;
	private int hour;
	private int minute;
	private OnTimeChangeListener timeChangeListener = null;
	public TimeWheelView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.wheel_time_view, this, true);
		initData();
		initView();
		initListener();
	}

	/**
	 * Constructor
	 */
	public TimeWheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.wheel_time_view, this, true);
		initData();
		initView();
		initListener();
	}

	public void setOnTimeChangeListener(OnTimeChangeListener l){
		this.timeChangeListener = l;
	}
	private void initData() {
		mCalendar = Calendar.getInstance();
		halfDay = mCalendar.get(Calendar.AM_PM);
		hour = mCalendar.get(Calendar.HOUR_OF_DAY);
		minute = mCalendar.get(Calendar.MINUTE);
	}

	public void setTime(int hour, int minute, int halfDay) {
		this.halfDay = halfDay;
		this.hour = hour;
		this.minute = minute;

	}
	
	public void setHalfDay(int halfDay) {
		this.halfDay = halfDay;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	private void initView() {
		mCalendar = Calendar.getInstance();

		wvHalfDay = (WheelView) findViewById(R.id.wheelView_halfday);
		wvHour = (WheelView) findViewById(R.id.wheelView_hour);
		wvMinute = (WheelView) findViewById(R.id.wheelView_minute);
		
		// 半天
		List<String> list = new ArrayList<String>();
		list.add("上午");
		list.add("下午");
		wvHalfDay.setAdapter(new TextWheelAdapter(list));// 设置"时"的显示数据
		wvHalfDay.setCyclic(false);// 可循环滚动
		wvHalfDay.setCurrentItem(halfDay);// 初始化时显示的数据
		wvHalfDay.setVisibleItems(3);
		// 时
		wvHour.setAdapter(new NumericWheelAdapter(0, 12, "%02d"));// 设置"时"的显示数据
		wvHour.setCyclic(true);// 可循环滚动
		wvHour.setCurrentItem(hour);// 初始化时显示的数据
		// 分
		wvMinute.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		wvMinute.setCyclic(true);
		wvMinute.setCurrentItem(minute);

		setValueTextSize(30);
		setItemTextSize(26);
	}

	private void initListener() {
		wvMinute.addChangingListener(this);
		wvHour.addChangingListener(this);
		wvHalfDay.addChangingListener(this);	

	}

	public void setItemBackground(int resid) {
		wvHalfDay.setBackgroundResource(resid);
		wvHour.setBackgroundResource(resid);
		wvMinute.setBackgroundResource(resid);
	}

	public void setCenterImage(int resid) {
		wvHalfDay.setCenterImage(resid);
		wvHour.setCenterImage(resid);
		wvMinute.setCenterImage(resid);
	}
	
	public int getHalfDay() {
		if (wvHalfDay == null) {
			return halfDay;
		}
		return wvHalfDay.getCurrentItem();
	}

	public int getHour() {
		if (wvHour == null) {
			return hour;
		}
		return wvHour.getCurrentItem();
	}

	public int getMinute() {
		if (wvMinute == null) {
			return minute;
		}
		return wvMinute.getCurrentItem();
	}

	public void setItemTextSize(int textSize) {
		textSize = DisplayUtil.sp2px(textSize);
		wvHalfDay.TEXT_SIZE_ITEM = textSize;
		wvMinute.TEXT_SIZE_ITEM = textSize;
		wvHour.TEXT_SIZE_ITEM = textSize;		
	}

	public void setValueTextSize(int textSize) {
		textSize = DisplayUtil.sp2px(textSize);
		wvHalfDay.TEXT_SIZE_VALUE = textSize;
		wvMinute.TEXT_SIZE_VALUE = textSize;
		wvHour.TEXT_SIZE_VALUE = textSize;		
	}
	
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wvMinute == wheel) {
			if (oldValue == 0 && newValue == 59) {
				wvHour.setCurrentItem(wvHour.getCurrentItem() - 1);
			}else if (oldValue == 59 && newValue == 0) {
				wvHour.setCurrentItem(wvHour.getCurrentItem() + 1);
			}
		}
		if (timeChangeListener != null) {
			timeChangeListener.onChange(wvHalfDay.getCurrentItem(), wvHour.getCurrentItem(), wvMinute.getCurrentItem());
		}
	}

	public interface OnTimeChangeListener{
		public void onChange(int type, int hour, int minute);
	}
}
