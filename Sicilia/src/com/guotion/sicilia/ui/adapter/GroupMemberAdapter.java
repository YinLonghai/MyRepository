package com.guotion.sicilia.ui.adapter;

import java.util.List;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.ui.view.GroupItemView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @function 联系人、聊天组  界面中listview的adapter
 *
 * @author   Longhai.Yin  MailTo: 1195219040 @ qq.com
 *
 * @version  NO.01
 *
 * @create   2014-4-7 下午5:09:42
 *
 */
public class GroupMemberAdapter extends BaseAdapter {

	private List<ChatGroup> list;
	private LayoutInflater inflater;
	private Context context;
	public GroupMemberAdapter() {}

	public GroupMemberAdapter(Context context, List<ChatGroup> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GroupItemView view;
		if (convertView == null) {
			view = new GroupItemView(context);
			convertView = view;
			convertView.setTag(view);
		} else {
			view = (GroupItemView) convertView.getTag();
		}
		view.setData(list.get(position));
		return convertView;
	}
	
	static class ViewHolder {
		public ImageView listImage;
		public TextView listName;
	}

	public void setList(List<ChatGroup> searchChatGroups) {
		this.list = searchChatGroups;
		notifyDataSetInvalidated();
	}
}
