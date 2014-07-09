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
import com.guotion.sicilia.util.TextBold;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityChildItemView extends RelativeLayout{
	private ImageView avatar;
	private TextView tvName;
	private TextView date;
	private TextView desc;

	private Activity activityInfo;
	public ActivityChildItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public ActivityChildItemView(Context context) {
		super(context);
		initView();
	}
	private void initView(){
		LayoutInflater.from(getContext()).inflate(R.layout.listview_item_child_activity, this);
		avatar = (ImageView) findViewById(R.id.imageView_avatar);
		date = (TextView) findViewById(R.id.textView_date);
		desc = (TextView) findViewById(R.id.textView_desc);
		tvName = (TextView)findViewById(R.id.textView_name);
		TextBold.setTextBold(tvName);
	}

	public void setData(Activity activityInfo) {
		this.activityInfo = activityInfo;
		initData();
	}
	private void initData(){
		tvName.setText(activityInfo.name);
		date.setText(activityInfo.date);
		desc.setText(activityInfo.content);
	}
	public void initNetImg(){
		User user = new Gson().fromJson(activityInfo.creator+"", User.class);
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
