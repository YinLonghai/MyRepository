package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;
import java.util.LinkedList;

import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.ConversationInfo;
import com.guotion.sicilia.ui.view.ConversationItemView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConversationAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private LinkedList<ConversationInfo> mMessageItems = null;
	private Context context = null;

	public ConversationAdapter(Context context,
			LinkedList<ConversationInfo> mMessageItems) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.mMessageItems = mMessageItems;
	}

	@Override
	public int getCount() {
		return mMessageItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mMessageItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ConversationItemView conversationItemView;
		if (convertView == null) {
			conversationItemView = new ConversationItemView(context);
			convertView = conversationItemView;
			convertView.setTag(conversationItemView);
		}else{
			conversationItemView = (ConversationItemView) convertView.getTag();
		}
		conversationItemView.setConversationInfo(mMessageItems.get(position));
		return convertView;
	}

}
