package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;

import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.ActivityInfo;
import com.guotion.sicilia.bean.net.ANotification;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.ui.GroupChatActivity;
import com.guotion.sicilia.util.DisplayUtil;
import com.guotion.sicilia.util.PreferencesHelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ActivityNoteAdapter extends BaseAdapter{
	private ArrayList<ANotification> list ;
	private Context context = null;
	int imgResId;
	public ActivityNoteAdapter(ArrayList<ANotification> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		int theme = new PreferencesHelper(context).getInt(AppData.THEME);
		try {
			imgResId = AppData.getThemeImgResId(theme, "back_notice");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewCache viewHolder;
		if (convertView == null) {
			viewHolder = new ViewCache();
			viewHolder.activity = new TextView(context);
			viewHolder.activity.setPadding(DisplayUtil.dip2px(30), DisplayUtil.dip2px(45), DisplayUtil.dip2px(30), DisplayUtil.dip2px(30));
			viewHolder.activity.setBackgroundResource(imgResId);
			convertView = viewHolder.activity;
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewCache) convertView.getTag();
		}
		viewHolder.activity.setText(list.get(position).content);
		return convertView;
	}
	
	private final class ViewCache{
		public TextView activity;
	}
}
