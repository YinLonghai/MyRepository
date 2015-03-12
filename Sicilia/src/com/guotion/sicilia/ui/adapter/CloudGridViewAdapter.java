package com.guotion.sicilia.ui.adapter;

import java.util.List;

import com.guotion.common.PictureBrowser.PictureBrowseView;
import com.guotion.common.utils.CacheUtil;
import com.guotion.sicilia.R;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.util.DisplayUtil;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CloudGridViewAdapter extends BaseAdapter {
	
	private List<String> picture;
	private Context context;
	
	public CloudGridViewAdapter(Context context, List<String> picture){
		this.context = context;
		this.picture = picture;
	}

	@Override
	public int getCount() {
		return picture.size();
	}

	@Override
	public Object getItem(int arg0) {
		return picture.get(arg0);
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
			viewHolder.ivPicture = new ImageView(context);
			convertView = viewHolder.ivPicture;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
//		if (picture.get(position) == null || picture.get(position).equals("")) {
//			viewHolder.ivPicture.setImageResource(R.drawable.default_img);
//		}
		viewHolder.ivPicture.setImageResource(R.drawable.default_img);
		viewHolder.ivPicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(context,R.style.dialog_download_full_screen);
				PictureBrowseView pictureBrowseView = new PictureBrowseView(context);
				String url = picture.get(position);
				pictureBrowseView.setImageUrls(new String[]{ChatServerConstant.URL.SERVER_HOST+url}, new String[]{CacheUtil.cloudImageCachePath+url.substring(url.lastIndexOf("/"))}, null);
//				pictureBrowseView.setFocusable(true);
//				pictureBrowseView.setFocusableInTouchMode(true);
				pictureBrowseView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {System.out.println("aaaaaaa");
						dialog.dismiss();
					}
				});
				dialog.setContentView(pictureBrowseView);
				dialog.show();
			}
		});
		return convertView;
	}
	
	final static class ViewHolder{
		ImageView ivPicture;
	}

}
