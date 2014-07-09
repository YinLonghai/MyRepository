package com.guotion.sicilia.ui.view;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GroupItemView extends RelativeLayout{
	private ImageView listImage;
	private TextView listName;

	private ChatGroup entity;
	public GroupItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}

	public GroupItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	private void initData(){
		listName.setText(entity.GroupName);
		listName.setTextColor(Color.BLACK);
	}
	private void initView(){
		LayoutInflater.from(getContext()).inflate(R.layout.group_member_item, this);
		listImage = (ImageView) findViewById(R.id.list_image);
		listName = (TextView) findViewById(R.id.list_name);
	}
	private void initListener(){
		
	}
	public void initNetImg(){
		// 设置头像 图片
		String imgUrl = entity.GroupPhoto;
		if (imgUrl != null && !imgUrl.equals("")) {
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(
					CacheUtil.avatarCachePath
							+ imgUrl.substring(imgUrl.lastIndexOf("/")));
			if (bitmap == null) {
				AppData.volleyUtil.loadImageByVolley(
						ChatServerConstant.URL.SERVER_HOST + entity.GroupPhoto,
						listImage, R.drawable.head_team_orang,
						R.drawable.head_team_orang);
			} else {
				listImage.setImageBitmap(bitmap);
			}
		} else {
			listImage.setImageResource(R.drawable.head_team_orang);
		}
	}
	public void setData(ChatGroup chatGroup){
		entity = chatGroup;
		initData();
	}
}
