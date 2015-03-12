package com.guotion.sicilia.ui.view;

import com.google.gson.Gson;
import com.guotion.common.PictureBrowser.SimpleNetImageView;
import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.common.utils.MyUtil;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.SignatureHistory;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.util.TextBold;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContastItemView extends RelativeLayout{
	private SimpleNetImageView listImage;
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
		listName.setText(entity.nickName);
//		System.out.println(entity.signature.equals("null"));
		if(!entity.signature.equals("null")){//System.out.println("signature="+entity.signature);
			SignatureHistory signatureHistory = new Gson().fromJson(entity.signature+"", SignatureHistory.class);
			listNote.setText(signatureHistory.content);
		}else{
			listNote.setText("");
		}
		TextBold.setTextBold(listName);
		TextBold.setTextBold(listNote);
		listImage.setImageResource(R.drawable.head_b);
		initNetImg();
	}
	private void initView(){
		LayoutInflater.from(getContext()).inflate(R.layout.contasts_item, this);
		listImage = (SimpleNetImageView) findViewById(R.id.list_image);
		listName = (TextView) findViewById(R.id.list_name);
		listNote = (TextView) findViewById(R.id.list_note);
	}
	private void initListener(){
		
	}
	Bitmap head;
	public void initNetImg(){
		// 设置头像 图片 "/images/UserLogo/52dd48496842472078000003.png"
		final String imgUrl = entity.headPhoto;
		if (imgUrl != null && !imgUrl.equals("")) {
			String cachePath = CacheUtil.avatarCachePath+ imgUrl.substring(imgUrl.lastIndexOf("/"));
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(cachePath);
			if (bitmap == null) {
//				AppData.volleyUtil.loadImageByVolley(
//						ChatServerConstant.URL.SERVER_HOST + entity.headPhoto,
//						listImage, R.drawable.head_b, R.drawable.head_b);
				listImage.loadImage(ChatServerConstant.URL.SERVER_HOST + entity.headPhoto, cachePath, R.drawable.head_b);
				
			} else {System.out.println(entity.userName+"-----"+entity.headPhoto);
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
