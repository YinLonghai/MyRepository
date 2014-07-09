package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;

import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.UserGroupInfo;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.ui.view.MemberChildItemView;
import com.guotion.sicilia.util.DisplayUtil;
import com.guotion.sicilia.util.TextBold;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class GenderMemberAdapter extends BaseExpandableListAdapter {
	private Context context = null;
	private ArrayList<UserGroupInfo> list;
	private boolean isSelectAll = false;
	private ArrayList<User> choose;
	public GenderMemberAdapter(Context context, ArrayList<UserGroupInfo> list) {
		super();
		this.context = context;
		this.list = list;
		choose = new ArrayList<User>();
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return list.get(arg0).list.get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	@Override
	public View getChildView(final int arg0, final int arg1, boolean arg2, View convertView,
			ViewGroup arg4) {
		final MemberChildItemView view;
		if (convertView == null) {
			view = new MemberChildItemView(context);
			convertView = view;
			convertView.setTag(view);
		}else{
			view = (MemberChildItemView) convertView.getTag();
		}
		view.setData(list.get(arg0).list.get(arg1));
		view.setSelect(isSelectAll);
		view.select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				if(cb.isChecked()){
					choose.add(list.get(arg0).list.get(arg1));
				}else{
					choose.remove(list.get(arg0).list.get(arg1));
				}
			}
		});
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(view.select.isChecked()){
					choose.remove(list.get(arg0).list.get(arg1));
					view.select.setChecked(false);
				}else{
					choose.add(list.get(arg0).list.get(arg1));
					view.select.setChecked(true);
				}
			}
		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0).list.size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View convertView, ViewGroup arg3) {
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
		viewHolder.name.setText(list.get(arg0).name);
		viewHolder.name.setTextSize(DisplayUtil.dip2px(10));
		TextBold.setTextBold(viewHolder.name);
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
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	public void setSelectAll(boolean isSelectAll) {
		this.isSelectAll = isSelectAll;
		choose.clear();
		if(isSelectAll){
			for(int i=0;i<list.size();i++){
				choose.addAll(list.get(i).list);
			}
		}
		notifyDataSetChanged();
	}
	public ArrayList<User> getChoose() {
		return choose;
	}
}
