package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;

import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.UserGroupInfo;
import com.guotion.sicilia.ui.dialog.ChooseDialog;
import com.guotion.sicilia.ui.dialog.ChooseDialog.ChooseClickListener;
import com.guotion.sicilia.ui.view.GroupMemberChildItemView;
import com.guotion.sicilia.util.DisplayUtil;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class GroupMemberExAdapter extends BaseExpandableListAdapter{
	public ArrayList<UserGroupInfo> list;
	private Context context = null;
	private ChooseDialog chooseDialog = null;
	
	public GroupMemberExAdapter(ArrayList<UserGroupInfo> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition).list.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		GroupMemberChildItemView view;
		if (convertView == null) {
			view = new GroupMemberChildItemView(context);
			convertView = view;
			convertView.setTag(view);
		}else{
			view = (GroupMemberChildItemView) convertView.getTag();
		}
		view.setData(list.get(groupPosition).list.get(childPosition));
//		if (groupPosition == 1 || groupPosition == 2) {			
//			convertView.setOnLongClickListener(new OnLongClickListener() {
//				
//				@Override
//				public boolean onLongClick(View v) {
//					if (chooseDialog == null) {
//						chooseDialog = new ChooseDialog(context);
//						chooseDialog.setOnChooseClickListener(new ChooseClickListener() {
//							
//							@Override
//							public void sureClick() {
//								if (groupPosition == 1) {
//									
//								} else {
//									
//								}
//							}
//						});
//					}
//					if (groupPosition == 1) {
//
//						chooseDialog.setText("移至普通成员");
//					} else {
//
//						chooseDialog.setText("移至管理员");
//					}
//					chooseDialog.show();
//					return true;
//				}
//			});
//		} 
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition).list.size();
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
		if (convertView == null) {
			viewHolder = new ViewCache();
			viewHolder.name = new TextView(context);
			viewHolder.name.setHeight(DisplayUtil.dip2px(40));
			viewHolder.name.setPadding(DisplayUtil.dip2px(25), 0, 0, 0);
			viewHolder.name.setGravity(Gravity.CENTER_VERTICAL);
			viewHolder.name.setBackgroundResource(R.color.lightgrey);
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
