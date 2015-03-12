package com.guotion.sicilia.ui.view;

import com.google.gson.Gson;
import com.guotion.common.PictureBrowser.SimpleNetImageView;
import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.CloudItemInfo;
import com.guotion.sicilia.bean.net.CloudEvent;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.util.TextBold;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CloudGridItemView extends LinearLayout{
	private SimpleNetImageView cloudHead;
	private TextView name;
	private TextView date;
	
	private CloudEvent cloudItemInfo;

	public CloudGridItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public CloudGridItemView(Context context) {
		super(context);
		initView();
	}
	private void initView(){
		LayoutInflater.from(getContext()).inflate(R.layout.gridview_item_cloud, this);
		cloudHead = (SimpleNetImageView) findViewById(R.id.imageView_gridview_cloud_head);
		name = (TextView) findViewById(R.id.textView_name);
		TextBold.setTextBold(name);
		date = (TextView) findViewById(R.id.textView_date);
	}

	public void setData(CloudEvent cloudItemInfo) {
		this.cloudItemInfo = cloudItemInfo;
		initData();
	}
	private void initData(){
		name.setText(cloudItemInfo.name);
		date.setText(cloudItemInfo.date);
		cloudHead.setImageResource(R.drawable.cloud_thumbnail_bg);
	}
	public void initNetImg(){
		User user = new Gson().fromJson(cloudItemInfo.owner+"", User.class);
		String imgUrl = user.headPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			String cachePath = CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/"));
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(cachePath);
			if(bitmap == null){
				cloudHead.loadImage(ChatServerConstant.URL.SERVER_HOST+user.headPhoto, cachePath, R.drawable.cloud_thumbnail_bg);
//				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+user.headPhoto, cloudHead, R.drawable.cloud_thumbnail_bg, R.drawable.cloud_thumbnail_bg);
			}else{
				cloudHead.setImageBitmap(bitmap);
			}
		}else{
			cloudHead.setImageResource(R.drawable.cloud_thumbnail_bg);
		}
	}
}
