package com.guotion.sicilia.ui.adapter;

import java.util.List;

import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.ui.view.CommentItemView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CommentAdapter extends BaseAdapter{

	private List<ChatItem> list ;
	private Context context = null;
	
	public CommentAdapter(List<ChatItem> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommentItemView view;
		if (convertView == null) {
			view = new CommentItemView(context);
			convertView = view;
			convertView.setTag(view);
		}else{
			view = (CommentItemView) convertView.getTag();
		}
		view.setData(list.get(position));
		return convertView;
	}

}
