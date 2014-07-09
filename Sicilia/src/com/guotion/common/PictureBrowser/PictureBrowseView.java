package com.guotion.common.PictureBrowser;



import com.guotion.sicilia.R;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class PictureBrowseView extends RelativeLayout {
	private PictureBrowseAdapter adapter = null;
	private PagePointView ppv;
	private String[] imageUrls; //图片的网络地址
	private String[] imagePaths;//图片的本地路径
	private String[] thumbnailPaths;//缩略图的本地路径
	private ViewPager pager;
	private int startPosition ;
	
	private DownloadListener downloadListener;
	
	public PictureBrowseView(Context context) {
		super(context);
		initView(context);
		initListener();
	}
	public PictureBrowseView(Context context,DownloadListener downloadListener) {
		super(context);
		this.downloadListener = downloadListener;
		initView(context);
		initListener();
	}
	
	public void setImageUrls(String[] imageUrls,String[] imagePaths,String[] thumbnailPaths){
		this.setImageUrls(imageUrls,imagePaths,thumbnailPaths,0);
	}
	public void setImageUrls(String[] imageUrls,String[] imagePaths,String[] thumbnailPaths,int startPosition){
		this.imageUrls = imageUrls;
		this.imagePaths = imagePaths;
		this.thumbnailPaths = thumbnailPaths;
		this.startPosition = startPosition;
		initData();
	}
	
	private void initData() {
		ppv.setTotalNum(imageUrls.length);		
		ppv.setPosition(startPosition);		
		adapter = new PictureBrowseAdapter(getContext(), imageUrls,imagePaths,thumbnailPaths,startPosition,downloadListener);
		pager.setAdapter(adapter);
		pager.setCurrentItem(startPosition);
	}

	public PictureBrowseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		initListener();
	}

	private void initView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.view_picture_browse, this, true);
		ppv = (PagePointView)findViewById(R.id.pagePointView_pictureBrowse);
		pager = (ViewPager)findViewById(R.id.contentPager);
		
	}

	private void initListener() {
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				ppv.setPosition(arg0);
				if(adapter != null)
					adapter.initPicture(arg0);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
	
		});
		
	}
	
	public void setDownloadListener(DownloadListener downloadListener){
		
	}
}
