package com.guotion.sicilia.ui.adapter;

import java.util.List;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.util.AndroidFileUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class NetImageAdapter extends BaseAdapter {
	private List<String> imageUrls = null;
	private Context context = null;
	public NetImageAdapter(Context context, List<String> imageUrls){
		this.context = context;
		this.imageUrls = imageUrls;
	}
	@Override
	public int getCount() {
		return imageUrls.size();
	}

	@Override
	public String getItem(int position) {
		return imageUrls.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(context);
			convertView = imageView;
			convertView.setTag(imageView);
		}else {
			imageView = (ImageView) convertView.getTag();
		}
		imageView.setImageResource(R.drawable.default_img);
		String imgUrl = imageUrls.get(position);
		if(CacheUtil.getInstence().iSCacheFileExists(imgUrl)){
			imageView.setImageBitmap(AndroidFileUtils.getBitmap(imgUrl, 400, 400));
		}else{
			if(imgUrl != null && !imgUrl.equals("")){
				Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
				if(bitmap == null){
//					new NetImageTask(imageView).execute(position);
					AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST + imgUrl, imageView, R.drawable.default_img, R.drawable.default_img);
				}else {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
		
		return convertView;
	}

//	private class NetImageTask extends AsyncTask<Integer, Integer, Bitmap>  {
//		private ImageView imageView = null;
//		public NetImageTask(ImageView imageView){
//			this.imageView = imageView;
//		}
//		@Override
//		protected Bitmap doInBackground(Integer... params) {
//			String url = ChatServerConstant.URL.SERVER_HOST + imageUrls.get(params[0]);
//			LogUtil.i("url=" + url);
//			Bitmap bitmap = QQUtil.getbitmap(url);
//			LocalImageCache.get().putCache(imageUrls.get(params[0]), bitmap);
//			return bitmap;
//		}
//
//		@Override
//		protected void onPostExecute(Bitmap result) {
//			if (result != null) {
//				imageView.setImageBitmap(result);
//			}
//			super.onPostExecute(result);
//		}
//		
//	}
}
