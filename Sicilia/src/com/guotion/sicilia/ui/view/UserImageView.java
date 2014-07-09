package com.guotion.sicilia.ui.view;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

public class UserImageView extends ImageView{
	public UserImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public UserImageView(Context context){
		super(context);
	}
	
	public void setUser(User user){
		if (user != null) {
			String imgUrl = user.headPhoto;
			if(imgUrl != null && !imgUrl.equals("")){
				Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
				if(bitmap == null){
					AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+user.headPhoto,this,R.drawable.head_m,R.drawable.head_m);
				}else{
					setImageBitmap(bitmap);
				}
			}else{
				setImageResource(R.drawable.head_m);
			}
			
		}
	}

}
