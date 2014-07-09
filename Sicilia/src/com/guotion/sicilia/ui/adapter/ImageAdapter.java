package com.guotion.sicilia.ui.adapter;

import java.util.LinkedList;
import java.util.List;

import com.guotion.sicilia.util.AndroidFileUtils;
import com.guotion.sicilia.util.DisplayUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageAdapter extends BaseAdapter {
	
	private List<String> picture = null;
	private Context context = null;
	private List<Bitmap> bitmaps = null;
	public ImageAdapter(Context context, List<String> picture){
		this.context = context;
		this.picture = picture;
		bitmaps = new LinkedList<Bitmap>();
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
			viewHolder.ivPicture.setScaleType(ScaleType.CENTER_CROP);
			convertView = viewHolder.ivPicture;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		Bitmap bitmap = AndroidFileUtils.getBitmap(picture.get(position), 150, 250);
		bitmaps.add(bitmap);
		viewHolder.ivPicture.setImageBitmap(bitmap);
		return convertView;
	}
	
	final static class ViewHolder{
		ImageView ivPicture;
	}

	public void clear() {
		for (Bitmap bitmap:bitmaps) {
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}
		bitmaps.clear();
		picture.clear();
	}

}
