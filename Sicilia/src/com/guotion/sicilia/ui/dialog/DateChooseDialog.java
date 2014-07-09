package com.guotion.sicilia.ui.dialog;

import com.guotion.sicilia.R;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.ui.view.DateWheelView;
import com.guotion.sicilia.ui.view.TimeWheelView;
import com.guotion.sicilia.util.PreferencesHelper;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DateChooseDialog extends Dialog implements OnClickListener{
	private DateWheelView dateWheelView = null;
	private TimeWheelView timeWheelView = null;
	private RelativeLayout rlTitle = null;
	private TextView tvSubmit = null;
	private TextView tvTitle = null;
	private TextView tvBack = null;
	private LinearLayout llBack = null;
	
	private Context context;
	private OnChooseDateListener chooseDateListener = null;
	public DateChooseDialog(Context context) {
		super(context, R.style.dialog_full_screen);
		this.context = context;
		initView();
		initListener();
	}

	public void setTitle(String text){
		tvTitle.setText(text);
	}
	
	public void setTitle(int resid){
		tvTitle.setText(resid);
	}
	
	public void setDate(int year, int month, int day){
		dateWheelView.setYear(year);
		dateWheelView.setMonth(month);
		dateWheelView.setDay(day);
	};
	
	public void setTime(int hour, int minute){
		timeWheelView.setHalfDay(hour/12);
		timeWheelView.setHour(hour%12);
		timeWheelView.setMinute(minute);
	}
	
	public void setOnChooseDateListener(OnChooseDateListener l){
		this.chooseDateListener = l;
	}
	
	private void initView() {
		setContentView(R.layout.dialog_choose_date);
		rlTitle = (RelativeLayout)findViewById(R.id.relativeLayout_top);
		
		dateWheelView = (DateWheelView)findViewById(R.id.dateWheelView_choose_date);
		timeWheelView = (TimeWheelView)findViewById(R.id.timeWheelView_choose_date);
		
		tvBack = (TextView)findViewById(R.id.textView_back);
		tvTitle = (TextView)findViewById(R.id.textView_title);
		tvSubmit = (TextView)findViewById(R.id.textView_submit);
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		updateTheme();
	}
	private void updateTheme() {
		int theme = new PreferencesHelper(context).getInt(AppData.THEME);
		int color;
		try {
			switch (theme) {
			case AppData.THEME_MALE:
				rlTitle.setBackgroundResource(AppData.getThemeColor(theme));
				tvBack.setTextColor(context.getResources().getColor(R.color.white));
				tvSubmit.setTextColor(context.getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				rlTitle.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = context.getResources().getColor(AppData.getThemeColor(theme));
				tvBack.setTextColor(color);
				tvSubmit.setTextColor(color);
				break;
			case AppData.THEME_BLUE:
				rlTitle.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = context.getResources().getColor(AppData.getThemeColor(theme));
				tvBack.setTextColor(color);
				tvSubmit.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				rlTitle.setBackgroundResource(AppData.getThemeColor(theme));
				tvBack.setTextColor(context.getResources().getColor(R.color.white));
				tvSubmit.setTextColor(context.getResources().getColor(R.color.white));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void initListener() {
		tvSubmit.setOnClickListener(this);
		llBack.setOnClickListener(this);
	}

	public interface OnChooseDateListener{
		public void onChooseDate(int year, int month, int day, int hour, int minute);
	}

	@Override
	public void onClick(View v) {
		if (v == tvSubmit) {
			if (chooseDateListener != null) {
				chooseDateListener.onChooseDate(dateWheelView.getYear(), 
						dateWheelView.getMonth(), 
						dateWheelView.getDay(),
						12*timeWheelView.getHalfDay() + timeWheelView.getHour(),
						timeWheelView.getMinute());
				
			}
		}
		dismiss();
	}
}
