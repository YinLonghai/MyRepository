package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.RegisterUserItemInfo;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @function 注册管理界面中listview的adapter
 *
 * @author   Longhai.Yin  MailTo: 1195219040 @ qq.com
 *
 * @version  NO.01
 *
 * @create   2014-4-7 下午5:09:42
 *
 */
public class StatisticAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<User> list;
	private LayoutInflater inflater;
	
	public StatisticAdapter() {}

	public StatisticAdapter(Context context, ArrayList<User> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
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
		// TODO Auto-generated method stub
		
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_activity_settings_item, null);
			holder = new ViewHolder();
			holder.listImage = (ImageView) convertView
					.findViewById(R.id.listImage);
			holder.listName = (TextView) convertView
					.findViewById(R.id.listName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final User entity = list.get(position);
		// 设置头像 图片
		String imgUrl = entity.headPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
			if(bitmap == null){
				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+entity.headPhoto, holder.listImage, R.drawable.head_m, R.drawable.head_m);
			}else{
				holder.listImage.setImageBitmap(bitmap);
			}
		}else{
			holder.listImage.setImageResource(R.drawable.head_b);
		}
		
		
		holder.listName.setText(entity.userName);
		holder.listName.setTextColor(Color.BLACK);
		
		return convertView;
	}
	
	static class ViewHolder {
		public ImageView listImage;
		public TextView listName;
	}
}
