package com.guotion.sicilia.ui.view;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.guotion.sicilia.R;
import com.guotion.sicilia.ui.adapter.NumericWheelAdapter;
import com.guotion.sicilia.ui.view.WheelView.OnWheelChangedListener;
import com.guotion.sicilia.util.DisplayUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class DateWheelView extends LinearLayout implements OnWheelChangedListener{
	
	private int startYear = 1950;
	private int endYear = 2050;
	private WheelView wvYear;
	private WheelView  wvMonth;
	private WheelView  wvDay;
	private Calendar mCalendar;
	private List<String> listBigMonths = null;
	private List<String> listLittleMonths = null;
	private String[] monthsBig = { "1", "3", "5", "7", "8", "10", "12" };
	private String[] monthsLittle = { "4", "6", "9", "11" };
	private int year;
	private int month;
	private int day;
	private OnDateChangedListener onDateChangedListener = null;
	public DateWheelView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.wheel_date_view, this, true);
		initData();
		initView();
		initListener();
	}
	
	/**
	 * Constructor
	 */
	public DateWheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.wheel_date_view, this, true);
		initData();
		initView();
		initListener();
	}
	
	
	
	private void initData() {
		mCalendar = Calendar.getInstance();
		year = mCalendar.get(Calendar.YEAR);
		month = mCalendar.get(Calendar.MONTH);
		day = mCalendar.get(Calendar.DATE);
		listBigMonths = Arrays.asList(monthsBig);
		listLittleMonths = Arrays.asList(monthsLittle);
	}

	
	public void setDate(int year, int month, int day){
		this.year = year;
		this.month = month;
		this.day = day;
		
	}
	
	public void setYear(int year){
		this.year = year;
		wvYear.setCurrentItem(year - startYear);
	}	
	
	public void setMonth(int month){
		this.month = month;
		wvMonth.setCurrentItem(month);
		
	}
	
	public void setDay(int day){
		this.day = day;
		wvDay.setCurrentItem(day - 1);
	}
	
	private void initView() {
		mCalendar = Calendar.getInstance();
		
		
		wvYear = (WheelView)findViewById(R.id.wheelView_year);
		wvMonth = (WheelView)findViewById(R.id.wheelView_month);
		wvDay = (WheelView)findViewById(R.id.wheelView_day);
		
		//年
		wvYear.setAdapter(new NumericWheelAdapter(startYear, endYear));// 设置"年"的显示数据
		wvYear.setCyclic(true);// 可循环滚动
		wvYear.setLabel("年");// 添加文字
		wvYear.setCurrentItem(year - startYear);// 初始化时显示的数据

		// 月
		wvMonth.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
		wvMonth.setCyclic(true);
		wvMonth.setLabel("月");
		wvMonth.setCurrentItem(month);

		// 日
		wvDay.setCyclic(true);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (listBigMonths.contains(String.valueOf(month + 1))) {
			wvDay.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
		} else if (listLittleMonths.contains(String.valueOf(month + 1))) {
			wvDay.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wvDay.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
			else
				wvDay.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
		}
		wvDay.setLabel("日");
		wvDay.setCurrentItem(day - 1);
		
	}

	private void initListener() {
		
		wvDay.addChangingListener(this);
		wvYear.addChangingListener(this);
		wvMonth.addChangingListener(this);
		setItemTextSize(26);
		setValueTextSize(30);
	}
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == wvYear) {
			int year_num = newValue + startYear;
			// 判断大小月及是否闰年,用来确定"日"的数据
			if (listBigMonths.contains(String.valueOf(wvMonth
					.getCurrentItem() + 1))) {
				wvDay.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
			} else if (listLittleMonths.contains(String.valueOf(wvMonth
					.getCurrentItem() + 1))) {
				wvDay.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
			} else {
				if ((year_num % 4 == 0 && year_num % 100 != 0)
						|| year_num % 400 == 0)
					wvDay.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
				else
					wvDay.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
			}
		}else if (wheel == wvYear){
			int month_num = newValue + 1;
			// 判断大小月及是否闰年,用来确定"日"的数据
			if (listBigMonths.contains(String.valueOf(month_num))) {
				wvDay.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
			} else if (listLittleMonths.contains(String.valueOf(month_num))) {
				wvDay.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
			} else {
				if (((wvYear.getCurrentItem() + startYear) % 4 == 0 && (wvYear
						.getCurrentItem() + startYear) % 100 != 0)
						|| (wvYear.getCurrentItem() + startYear) % 400 == 0)
					wvDay.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
				else
					wvDay.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
			}
		}
		if (onDateChangedListener != null) {
			onDateChangedListener.onDateChanged(DateWheelView.this, startYear + wvYear.getCurrentItem(), wvMonth.getCurrentItem() + 1, wvDay.getCurrentItem() + 1);		
		}
		
	}
	
	public void setYear(int startYear, int endYear) {
		this.startYear = startYear;
		this.endYear = endYear;
	}
	
	public void setItemBackground(int resid){
		wvYear.setBackgroundResource(resid);
		wvMonth.setBackgroundResource(resid);
		wvDay.setBackgroundResource(resid);
	}
	
	public void setCenterImage(int resid){
		wvYear.setCenterImage(resid);
		wvMonth.setCenterImage(resid);
		wvDay.setCenterImage(resid);
	}
	public int getYear(){
		if (wvYear == null) {
			return year;
		}
		return wvYear.getCurrentItem() + startYear;
	}
	
	public int getMonth(){
		if (wvMonth == null) {
			return month;
		}
		return wvMonth.getCurrentItem() + 1;
	}
	
	public int getDay(){
		if (wvDay == null) {
			return day;
		}
		return wvDay.getCurrentItem() + 1;
	}
	
	
	public void setItemTextSize(int textSize){
		textSize = DisplayUtil.sp2px(textSize);
		wvDay.TEXT_SIZE_ITEM = textSize;
		wvMonth.TEXT_SIZE_ITEM = textSize;
		wvYear.TEXT_SIZE_ITEM = textSize;
	}

	public void setValueTextSize(int textSize){
		textSize = DisplayUtil.sp2px(textSize);
		wvDay.TEXT_SIZE_VALUE = textSize;
		wvMonth.TEXT_SIZE_VALUE = textSize;
		wvYear.TEXT_SIZE_VALUE = textSize;
	}
	
	public void setOnDateChangedListener(OnDateChangedListener l){
		this.onDateChangedListener = l;
	}
	
	public interface OnDateChangedListener{
		public void onDateChanged(DateWheelView v, int year, int month, int day);
	}

	
}
