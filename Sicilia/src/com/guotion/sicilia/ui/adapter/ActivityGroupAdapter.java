package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;

import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.ActivityGroupInfo;
import com.guotion.sicilia.ui.view.ActivityChildItemView;
import com.guotion.sicilia.util.DisplayUtil;
import com.guotion.sicilia.util.TextBold;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ActivityGroupAdapter extends BaseExpandableListAdapter{
	private ArrayList<ActivityGroupInfo> list;
	private Context context = null;

	public ActivityGroupAdapter(ArrayList<ActivityGroupInfo> list,
			Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition).activityList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ActivityChildItemView view;
		if(convertView == null){
			view = new ActivityChildItemView(context);
			convertView = view;
			convertView.setTag(view);
		}else{
			view = (ActivityChildItemView) convertView.getTag();
		}
		view.setData(list.get(groupPosition).activityList.get(childPosition));
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition).activityList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewCache viewHolder;
		if(convertView == null){
			viewHolder = new ViewCache();
			viewHolder.name = new TextView(context);
			viewHolder.name.setHeight(DisplayUtil.dip2px(40));
			viewHolder.name.setPadding(DisplayUtil.dip2px(25), 0, 0, 0);
			viewHolder.name.setGravity(Gravity.CENTER_VERTICAL);
			viewHolder.name.setBackgroundResource(R.color.whitesmoke);
			viewHolder.name.setTextSize(DisplayUtil.dip2px(10));
			TextBold.setTextBold(viewHolder.name);
			convertView = viewHolder.name;
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewCache) convertView.getTag();
		}
		viewHolder.name.setText(list.get(groupPosition).name);
		return convertView;
	}
	private final class ViewCache{
		public TextView name;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
