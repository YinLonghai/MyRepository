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
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.TextBold;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CloudListItemView extends RelativeLayout{
	private SimpleNetImageView cloudHead;
	private TextView tvName;
	private TextView tvDate;
	
	private CloudEvent cloudItemInfo;

	public CloudListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public CloudListItemView(Context context) {
		super(context);
		initView();
	}
	private void initView(){
		LayoutInflater.from(getContext()).inflate(R.layout.listview_item_cloud, this);
		cloudHead = (SimpleNetImageView) findViewById(R.id.imageView_listview_cloud_head);
		tvName = (TextView)findViewById(R.id.textView_listview_cloud_name);
		tvDate = (TextView)findViewById(R.id.textView_listview_cloud_date);
		TextBold.setTextBold(tvName);
	}

	public void setData(CloudEvent cloudItemInfo) {
		this.cloudItemInfo = cloudItemInfo;
		initData();
	}
	private void initData(){
		tvName.setText(cloudItemInfo.name);
		tvDate.setText(cloudItemInfo.date);
		cloudHead.setImageResource(R.drawable.head_s);
	}
	public void initNetImg(){
		User user = new Gson().fromJson(cloudItemInfo.owner+"", User.class);
		if(cloudItemInfo.isPrivate.equals("1")){
			int theme = new PreferencesHelper(getContext()).getInt(AppData.THEME);
			try {
				cloudHead.setImageResource(AppData.getThemeImgResId(theme, "lock_s"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			String imgUrl = user.headPhoto;
			if(imgUrl != null && !imgUrl.equals("")){
				String cachePath = CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/"));
				Bitmap bitmap = LocalImageCache.get().loadImageBitmap(cachePath);
				if(bitmap == null){
//					AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+user.headPhoto, cloudHead, R.drawable.head_s, R.drawable.head_s);
					cloudHead.loadImage(ChatServerConstant.URL.SERVER_HOST+user.headPhoto, cachePath, R.drawable.head_s);
				}else{
					cloudHead.setImageBitmap(bitmap);
				}
			}else{
				cloudHead.setImageResource(R.drawable.head_s);
			}
		}
	}
}
