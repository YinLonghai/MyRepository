package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<User> lists;
	private Context context;
	public AccountAdapter(Context convertView, ArrayList<User> lists) {
		super();
		this.lists = lists;
		inflater = LayoutInflater.from(convertView);
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listview_item_account, null);
			viewHolder.headImageView = (ImageView)convertView.findViewById(R.id.img_listitem_account_head);
			viewHolder.seleteImageView = (ImageView)convertView.findViewById(R.id.img_listitem_account_selete);
			viewHolder.userNameTextView = (TextView)convertView.findViewById(R.id.tv_listitem_account_name);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		String imgUrl = lists.get(position).headPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
			if(bitmap == null){
				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+lists.get(position).headPhoto, viewHolder.headImageView, R.drawable.head_s, R.drawable.head_s);
			}else{
				viewHolder.headImageView.setImageBitmap(bitmap);
			}
		}else{
			viewHolder.headImageView.setImageResource(R.drawable.head_s);
		}
		
		if(AppData.getUser(context)._id.equals(lists.get(position)._id))
			viewHolder.seleteImageView.setVisibility(View.VISIBLE);
		viewHolder.userNameTextView.setText(lists.get(position).userName);
		return convertView;
	}
	
	class ViewHolder{
		ImageView headImageView;
		ImageView seleteImageView;
		TextView userNameTextView;
	}
}