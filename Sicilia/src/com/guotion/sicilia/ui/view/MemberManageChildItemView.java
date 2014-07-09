package com.guotion.sicilia.ui.view;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.util.PreferencesHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MemberManageChildItemView extends RelativeLayout{
	private ImageView avatar;
	private TextView name;
	private ImageView select;
	private ImageView delete;
	private TextView tvDelete;
	
	private User user;
	int selectOnImgResId;
	int selectOffImgResId;
	int selectState = 0;//0-selectOn;1-selectOff
	
	private OnClickListener clickListener;
	

	public MemberManageChildItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		initListener();
	}

	public MemberManageChildItemView(Context context) {
		super(context);
		initView();
		initListener();
	}
	private void initView(){
		LayoutInflater.from(getContext()).inflate(R.layout.listview_item_child_member_manage, this);
		avatar = (ImageView) findViewById(R.id.imageView_avatar);
		name = (TextView) findViewById(R.id.textView_name);
		select = (ImageView) findViewById(R.id.imageView_select);
		delete = (ImageView) findViewById(R.id.imageView_delete);
		tvDelete = (TextView)findViewById(R.id.textView_delete);
	}
	private void initListener(){
		
		clickListener = new MyClickListener();
		delete.setOnClickListener(clickListener);
		tvDelete.setOnClickListener(clickListener);
	}
	public void setData(User user) {
		this.user = user;
		initData();
	}
	private void initData(){
		int theme = new PreferencesHelper(getContext()).getInt(AppData.THEME);
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
		name.setText(user.userName);
	}
	
	private final class MyClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if (v == delete) {
				delete.setVisibility(View.GONE);
				select.setVisibility(View.GONE);
				tvDelete.setVisibility(View.VISIBLE);
			} else if(v == tvDelete){
				delete.setVisibility(View.VISIBLE);
				select.setVisibility(View.VISIBLE);
				tvDelete.setVisibility(View.GONE);
			}
		}
		
	}//end of MyClickListener
}
