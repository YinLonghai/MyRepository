package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;

import com.guotion.sicilia.bean.UserInfo;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.ui.view.MemberChildItemView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

public class AgeMemberAdapter extends BaseAdapter{
	private Context context = null;
	private ArrayList<User> list;
	private boolean isSelectAll = false;
	private ArrayList<User> choose;
	public AgeMemberAdapter(Context context, ArrayList<User> list) {
		super();
		this.context = context;
		this.list = list;
		choose = new ArrayList<User>();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final MemberChildItemView view;
		if(convertView == null){
			view = new MemberChildItemView(context);
			convertView = view;
			convertView.setTag(view);
		}else{
			view = (MemberChildItemView) convertView.getTag();
		}
		view.setData(list.get(position));
		view.setSelect(isSelectAll);
		view.select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				if(cb.isChecked()){
					choose.add(list.get(position));
				}else{
					choose.remove(list.get(position));
				}
			}
		});
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(view.select.isChecked()){
					choose.remove(list.get(position));
					view.select.setChecked(false);
				}else{
					choose.add(list.get(position));
					view.select.setChecked(true);
				}
			}
		});
		return convertView;
	}

	public void setSelectAll(boolean isSelectAll) {
		this.isSelectAll = isSelectAll;
		choose.clear();
		if(isSelectAll){
			choose.addAll(list);
		}
		notifyDataSetChanged();
	}

	public ArrayList<User> getChoose() {
		return choose;
	}
	
}
