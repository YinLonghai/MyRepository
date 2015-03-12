package com.guotion.sicilia.ui.view;

import com.google.gson.Gson;
import com.guotion.common.PictureBrowser.SimpleNetImageView;
import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.CloudInfo;
import com.guotion.sicilia.bean.UserInfo;
import com.guotion.sicilia.bean.net.CloudEvent;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CloudCheckItemView extends RelativeLayout{
	private SimpleNetImageView head;
	private TextView tvName;
	private TextView tvDesc;
	
	private User user;
	private CloudEvent cloud;

	public CloudCheckItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public CloudCheckItemView(Context context) {
		super(context);
		initView();
	}
	private void initView(){
		LayoutInflater.from(getContext()).inflate(R.layout.listview_item_cloud_check, this);
		head = (SimpleNetImageView) findViewById(R.id.imageView_head);
		tvName = (TextView) findViewById(R.id.textView_name);
		tvDesc = (TextView) findViewById(R.id.textView_desc);
	}
	private String headPath;
	private void initData(String headPath,String name,String desc){
		this.headPath = headPath;
		tvName.setText(name);
		tvDesc.setText(desc);
		head.setImageResource(R.drawable.head_m);
	}
	public void setUser(User user) {
		this.user = user;
		initData(user.headPhoto,user.userName,user.nickName);
	}

	public void setCloud(CloudEvent cloud) {
		this.cloud = cloud;
		User u = new Gson().fromJson(cloud.owner+"", User.class);
		initData(u.headPhoto,cloud.name,cloud.desc);
	}
	public void initNetImg(){
		if(headPath != null && !headPath.equals("")){
			String cachePath = CacheUtil.avatarCachePath+headPath.substring(headPath.lastIndexOf("/"));
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(cachePath);
			if(bitmap == null){
				head.loadImage(ChatServerConstant.URL.SERVER_HOST+headPath, cachePath, R.drawable.head_m);
//				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+headPath, head, R.drawable.head_m, R.drawable.head_m);
			}else{
				head.setImageBitmap(bitmap);
			}
		}else{
			head.setImageResource(R.drawable.head_m);
		}
	}
}
