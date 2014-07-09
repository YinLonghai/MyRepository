package com.guotion.sicilia.ui.view;

import com.google.gson.Gson;
import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.ActivityInfo;
import com.guotion.sicilia.bean.net.Activity;
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

public class BirthdayRemindItemView extends RelativeLayout{
	private TextView name;
	private TextView date;
	private TextView date2;
	private ImageView avatar;
	
	private Activity activityInfo;

	public BirthdayRemindItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public BirthdayRemindItemView(Context context) {
		super(context);
		initView();
	}
	private void initView(){
		LayoutInflater.from(getContext()).inflate(R.layout.listview_item_birthday_remind, this);
		name = (TextView) findViewById(R.id.textView_name);
		date = (TextView) findViewById(R.id.textView_date);
		date2 = (TextView) findViewById(R.id.textView_date2);
		avatar = (ImageView) findViewById(R.id.imageView_avatar);
	}

	public void setData(Activity activityInfo) {
		this.activityInfo = activityInfo;
		initData();
	}
	private User user;
	private void initData(){
		user = new Gson().fromJson(activityInfo.creator+"", User.class);
		if(user == null) return ;
		name.setText(user.userName);
		date.setText(user.birthday);
		date2.setText(user.lunarBtd);
	}
	public void initNetImg(){
		if(user == null) return ;
		String imgUrl = user.headPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
			if(bitmap == null){
				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+user.headPhoto, avatar, R.drawable.head_orang, R.drawable.head_orang);
			}else{
				avatar.setImageBitmap(bitmap);
			}
		}else{
			avatar.setImageResource(R.drawable.head_orang);
		}
	}
}
