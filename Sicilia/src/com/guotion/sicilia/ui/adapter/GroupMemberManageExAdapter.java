package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.UserGroupInfo;
import com.guotion.sicilia.ui.view.MemberManageChildItemView;
import com.guotion.sicilia.util.DisplayUtil;

public class GroupMemberManageExAdapter extends BaseExpandableListAdapter{
	public ArrayList<UserGroupInfo> list;
	private Context context = null;

	public GroupMemberManageExAdapter(ArrayList<UserGroupInfo> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return list.get(groupPosition).list.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		MemberManageChildItemView view;
		if (convertView == null) {
			view = new MemberManageChildItemView(context) ;
			convertView = view;
			convertView.setTag(view);
		}else{
			view = (MemberManageChildItemView) convertView.getTag();
		}
		view.setData(list.get(groupPosition).list.get(childPosition));
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return list.get(groupPosition).list.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return list.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return list.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewCache viewHolder;
		if (convertView == null) {
			viewHolder = new ViewCache();
			viewHolder.name = new TextView(context);
			viewHolder.name.setHeight(DisplayUtil.dip2px(40));
			viewHolder.name.setPadding(DisplayUtil.dip2px(25), 0, 0, 0);
			viewHolder.name.setGravity(Gravity.CENTER_VERTICAL);
			viewHolder.name.setBackgroundResource(R.color.gainsboro);
			convertView = viewHolder.name;
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewCache) convertView.getTag();
		}
		viewHolder.name.setText(list.get(groupPosition).name);
		viewHolder.name.setTextSize(DisplayUtil.dip2px(10));
		return convertView;
	}
	private final class ViewCache{
		public TextView name;
	}
	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
