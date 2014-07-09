package com.guotion.sicilia.ui.view;

import com.google.gson.Gson;
import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.CloudInfo;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserCloudItemView extends RelativeLayout{

	private ImageView avatar;
	private TextView name;
	private TextView date;
	
	private CloudEvent cloudInfo;
	
	public UserCloudItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public UserCloudItemView(Context context) {
		super(context);
		initView();
	}

	private void initView(){
		LayoutInflater.from(getContext()).inflate(R.layout.listview_item_user_cloud, this);
		avatar = (ImageView) findViewById(R.id.imageView_avatar);
		name = (TextView) findViewById(R.id.textView_name);
		TextBold.setTextBold(name);
		date = (TextView) findViewById(R.id.textView_date);
	}

	public void setCloudInfo(CloudEvent cloudInfo) {
		this.cloudInfo = cloudInfo;
		initData();
	}
	private void initData(){
		name.setText(cloudInfo.name);
		date.setText(cloudInfo.date);
	}
	public void initNetImg(){
		User user = new Gson().fromJson(cloudInfo.owner+"", User.class);
		String imgUrl = user.headPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
			if(bitmap == null){
				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+user.headPhoto,avatar,R.drawable.head_orang,R.drawable.head_orang);
			}else{
				avatar.setImageBitmap(bitmap);
			}
		}else{
			avatar.setImageResource(R.drawable.head_orang);
		}
	}
}
