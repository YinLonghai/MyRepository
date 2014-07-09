package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.db.AccountDBHelper;
import com.guotion.sicilia.db.UserDao;
import com.guotion.sicilia.im.constant.ChatServerConstant;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountManageAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<User> lists;
	
	private ArrayList<Integer> choose = new ArrayList<Integer>();

	public AccountManageAdapter(Context convertView, ArrayList<User> lists) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listview_item_account_manage, null);
			viewHolder.deleteImageView = (ImageView) convertView.findViewById(R.id.img_listitem_account_manage_delete);
			viewHolder.headImageView = (ImageView) convertView.findViewById(R.id.img_listitem_account_manage_head);
			viewHolder.userNameTextView = (TextView) convertView.findViewById(R.id.tv_listitem_account_manage_name);
			viewHolder.tvDelete = (TextView)convertView.findViewById(R.id.textView_delete);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
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
		
		viewHolder.userNameTextView.setText(lists.get(position).userName);
		viewHolder.deleteImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewHolder.deleteImageView.setVisibility(View.GONE);
				viewHolder.tvDelete.setVisibility(View.VISIBLE);
			}
		});
		
		viewHolder.tvDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				choose.add(lists.get(position).id);
				lists.remove(position);
				notifyDataSetChanged();
				viewHolder.deleteImageView.setVisibility(View.VISIBLE);
				viewHolder.tvDelete.setVisibility(View.GONE);
			}
		});
		viewHolder.headImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewHolder.deleteImageView.setVisibility(View.VISIBLE);
				viewHolder.tvDelete.setVisibility(View.GONE);
			}
		});
		viewHolder.userNameTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewHolder.deleteImageView.setVisibility(View.VISIBLE);
				viewHolder.tvDelete.setVisibility(View.GONE);
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView deleteImageView;
		ImageView headImageView;
		TextView userNameTextView;
		TextView tvDelete;
	}

	public ArrayList<Integer> getChoose() {
		return choose;
	}
	
}