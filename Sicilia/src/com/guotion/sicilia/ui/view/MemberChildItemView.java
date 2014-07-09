package com.guotion.sicilia.ui.view;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.UserInfo;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.util.PreferencesHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MemberChildItemView extends RelativeLayout{
	private ImageView avatar;
	private TextView name;
	public CheckBox select;
	
	private User user;
	private boolean isSelect = false;
	int selectOnImgResId;
	int selectOffImgResId;

	public MemberChildItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public MemberChildItemView(Context context) {
		super(context);
		initView();
	}
	private void initView(){
		LayoutInflater.from(getContext()).inflate(R.layout.listview_item_child_member, this);
		avatar = (ImageView) findViewById(R.id.imageView_avatar);
		name = (TextView) findViewById(R.id.textView_name);
		select = (CheckBox) findViewById(R.id.imageView_select);
	}

	public void setData(User user) {
		this.user = user;
		initData();
	}
	private void initData(){
		int theme = new PreferencesHelper(getContext()).getInt(AppData.THEME);
		try {
			select.setButtonDrawable(AppData.getThemeImgResId(theme, "checkbox_style"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		name.setText(user.userName);
	}
	public void initNetImg(){
		String imgUrl = user.headPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
			if(bitmap == null){
				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+user.headPhoto,avatar,R.drawable.head_s,R.drawable.head_s);
			}else{
				avatar.setImageBitmap(bitmap);
			}
		}else{
			avatar.setImageResource(R.drawable.head_s);
		}
	}
//	public void changeSelect() {
//		if(isSelect){
//			isSelect = false;
//			select.setImageResource(selectOffImgResId);
//		}else{
//			isSelect = true;
//			select.setImageResource(selectOnImgResId);
//		}
//	}
	public void hideSelect(){
		select.setVisibility(View.GONE);
	}

	public void setSelect(boolean isSelect) {
		select.setChecked(isSelect);
	}
	private ClickListener clickListener;
	
	public void setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public interface ClickListener{
		public void getUser(User user);
	}
}
