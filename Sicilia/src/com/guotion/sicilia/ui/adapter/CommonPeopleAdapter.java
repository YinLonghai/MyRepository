package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.UserInfo;
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

public class CommonPeopleAdapter extends BaseAdapter {
	
	private ArrayList<User> userInfos;
	private LayoutInflater layoutInflater;
	
	public CommonPeopleAdapter(Context context, ArrayList<User> userInfos){
		layoutInflater = LayoutInflater.from(context);
		this.userInfos = userInfos;
	}

	@Override
	public int getCount() {
		return userInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return userInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.gridview_people_common, null);
			viewHolder.ivAvatar = (ImageView)convertView.findViewById(R.id.ImageView_avatar);
			viewHolder.tvName = (TextView)convertView.findViewById(R.id.TextView_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
//		if ( userInfos.get(position).headPhoto == null || userInfos.get(position).headPhoto.equals(""))  {
//			viewHolder.ivAvatar.setImageResource(R.drawable.head_s);
//		} else {
//			viewHolder.tvName.setText(userInfos.get(position).userName);
//		}
		String imgUrl = userInfos.get(position).headPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
			if(bitmap == null){
				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+userInfos.get(position).headPhoto, viewHolder.ivAvatar, R.drawable.head_s, R.drawable.head_s);
			}else{
				viewHolder.ivAvatar.setImageBitmap(bitmap);
			}
		}else{
			viewHolder.ivAvatar.setImageResource(R.drawable.head_s);
		}
		
		viewHolder.tvName.setText(userInfos.get(position).userName);
		return convertView;
	}
	
	private static class ViewHolder{
		ImageView ivAvatar;
		TextView tvName;
	}

	public void setList(List<User> list){
		userInfos = (ArrayList<User>) list;
		notifyDataSetInvalidated();
	}
}
