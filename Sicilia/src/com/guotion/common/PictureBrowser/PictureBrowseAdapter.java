package com.guotion.common.PictureBrowser;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class PictureBrowseAdapter extends PagerAdapter{

	private ArrayList<NetImageView> views;
	private String[] imageUrls;
	private String[] imagePaths;
	private String[] thumbnailPaths;
	
//	private DownloadListener downloadListener;
	private OnClickListener onClickListener;
	
	public PictureBrowseAdapter(Context context, String[] imageUrls,String[] imagePaths,String[] thumbnailPaths,int position,DownloadListener downloadListener){
		this.imageUrls = imageUrls;
		this.imagePaths = imagePaths;
		this.thumbnailPaths = thumbnailPaths;
		views = new ArrayList<NetImageView>();
		for (int i = 0; i < imageUrls.length; i++) {
			NetImageView imageView = new NetImageView(context);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(params);
			if(downloadListener != null){
				imageView.setDownloadListener(downloadListener);
			}
			views.add(imageView);
		}
		//初始化时默认加载position位置的图片
		initPicture(position);
	}

	public void initPicture(int position){
		if(imagePaths != null){
			String imagePath = imagePaths[position];System.out.println(imagePath);
			if(new File(imagePath).exists()){
				views.get(position).setImagePath(imagePath);
				return ;
			}else{//System.out.println(imagePath.substring(0, imagePath.lastIndexOf("/")));
				views.get(position).setCacheFile(imagePath.substring(0, imagePath.lastIndexOf("/")));
			}
		}	
		if(thumbnailPaths != null)
			views.get(position).setThumbnailPath(thumbnailPaths[position]);
		views.get(position).setImageUrl(imageUrls[position]);System.out.println(imageUrls[position]);
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		for(NetImageView netImageView : views){
			netImageView.setOnClickListener(onClickListener);
		}
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		// TODO Auto-generated method stub
		((ViewPager) arg0).removeView(views.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return views.size();
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(views.get(arg1), 0);
		return views.get(arg1);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
