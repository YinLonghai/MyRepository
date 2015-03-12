package com.guotion.sicilia.ui.view;

import com.guotion.common.PictureBrowser.SimpleNetImageView;
import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.UserInfo;
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

public class GroupMemberChildItemView extends RelativeLayout{
	private SimpleNetImageView avatar;
	private TextView name;
	
	private User user;

	public GroupMemberChildItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public GroupMemberChildItemView(Context context) {
		super(context);
		initView();
	}
	private void initView(){
		LayoutInflater.from(getContext()).inflate(R.layout.listview_item_child_groupmember, this);
		avatar = (SimpleNetImageView) findViewById(R.id.imageView_avatar);
		name = (TextView) findViewById(R.id.textView_name);
	}

	public void setData(User user) {
		this.user = user;
		initData();
	}
	private void initData(){
		String imgUrl = user.headPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			String cachePath = CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/"));
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(cachePath);
			if(bitmap == null){
				avatar.setImageResource(R.drawable.head_s);
			}else{
				avatar.setImageBitmap(bitmap);
			}
		}else{
			avatar.setImageResource(R.drawable.head_s);
		}
		name.setText(user.nickName);
	}
	public void initNetImg(){
		String imgUrl = user.headPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			String cachePath = CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/"));
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(cachePath);
			if(bitmap == null){
//				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+user.headPhoto,avatar,R.drawable.head_s,R.drawable.head_s);
				avatar.loadImage(ChatServerConstant.URL.SERVER_HOST+user.headPhoto, cachePath, R.drawable.head_s);
			}else{
				avatar.setImageBitmap(bitmap);
			}
		}else{
			avatar.setImageResource(R.drawable.head_s);
		}
	}
}
