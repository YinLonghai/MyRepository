package com.guotion.sicilia.ui.adapter;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.ui.view.MsgLeftView;
import com.guotion.sicilia.ui.view.MsgRightView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ChatAdapter extends BaseAdapter {
	private Context context;

	private List<ChatItem> chatMessages;
	private Gson gson = new Gson();

	private String leftHeadPhotoPath;
	private String rightHeadPhotoPath;
	public ChatAdapter(Context context, LinkedList<ChatItem> messages ,String friendHeadPhotoPath) {
		super();
		this.context = context;
		this.chatMessages = messages;
		this.leftHeadPhotoPath = friendHeadPhotoPath;
	}

	@Override
	public int getCount() {
		return chatMessages.size();
	}

	@Override
	public Object getItem(int position) {
		return chatMessages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		ChatItem message = chatMessages.get(position);
		//User user = gson.fromJson(message.user+"", User.class);
		User user = message.userInfo;//System.out.println((user==null)+"---"+(user._id==null));
		if(user == null){
			user = AppData.getUser(message.user+"");
			if(user == null){
				user = new User();
				message.userInfo = new User();
			}else{
				message.userInfo = user;
			}
//			user = new User();
//			System.out.println("消息数据有问题------"+message.msg);
		}
		if (convertView == null ) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_chat, null);//生成条目界面对象
			holder.chatMessageLeftView = (MsgLeftView) convertView.findViewById(R.id.msgLeftView);
			holder.chatMessageRightView = (MsgRightView) convertView.findViewById(R.id.msgRightView);
			if (!user._id.equals(AppData.getUser(context)._id)) {
				holder.chatMessageLeftView.setChatItemInfo(message);
				holder.chatMessageRightView.setVisibility(View.GONE);
			}else {
				holder.chatMessageRightView.setChatItemInfo(message);
				holder.chatMessageLeftView.setVisibility(View.GONE);
			}
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
			if (!user._id.equals(AppData.getUser(context)._id)) {
				holder.chatMessageLeftView.setVisibility(View.VISIBLE);
				holder.chatMessageLeftView.setChatItemInfo(message);
				holder.chatMessageRightView.setVisibility(View.GONE);
			}else{
				holder.chatMessageRightView.setVisibility(View.VISIBLE);
				holder.chatMessageRightView.setChatItemInfo(message);
				holder.chatMessageLeftView.setVisibility(View.GONE);
			}
		}
		return convertView;
	}
	class ViewHolder {
		MsgLeftView chatMessageLeftView = null;
		MsgRightView chatMessageRightView = null;
		//int flag;
	}

}
