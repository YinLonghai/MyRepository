package com.guotion.sicilia.ui.view;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
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

public class ContastItemView extends RelativeLayout{
	private ImageView listImage;
	private TextView listName;
	private TextView listNote;

	private User entity;
	public ContastItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}

	public ContastItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	private void initData(){
		listName.setText(entity.userName);
		listNote.setText(entity.nickName);
		TextBold.setTextBold(listName);
		TextBold.setTextBold(listNote);
	}
	private void initView(){
		LayoutInflater.from(getContext()).inflate(R.layout.contasts_item, this);
		listImage = (ImageView) findViewById(R.id.list_image);
		listName = (TextView) findViewById(R.id.list_name);
		listNote = (TextView) findViewById(R.id.list_note);
	}
	private void initListener(){
		
	}
	public void initNetImg(){
		// 设置头像 图片 "/images/UserLogo/52dd48496842472078000003.png"
		String imgUrl = entity.headPhoto;
		if (imgUrl != null && !imgUrl.equals("")) {
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(
					CacheUtil.avatarCachePath
							+ imgUrl.substring(imgUrl.lastIndexOf("/")));
			if (bitmap == null) {
				AppData.volleyUtil.loadImageByVolley(
						ChatServerConstant.URL.SERVER_HOST + entity.headPhoto,
						listImage, R.drawable.head_b, R.drawable.head_b);
			} else {
				listImage.setImageBitmap(bitmap);
			}
		} else {
			listImage.setImageResource(R.drawable.head_b);
		}
	}
	public void setData(User user){
		entity = user;
		initData();
	}
}
