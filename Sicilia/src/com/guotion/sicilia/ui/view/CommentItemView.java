package com.guotion.sicilia.ui.view;

import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.ui.OtherInfoActivity;
import com.guotion.sicilia.util.UISkip;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CommentItemView extends RelativeLayout{
	private RelativeLayout rlLeft;
	private RelativeLayout rlRight;
	private ImageView avatarLeft;
	private TextView commentLeft;
	private ImageView avatarRight;
	private TextView commentRight;
	
	private ChatItem chatItem;
	private String userId;

	public CommentItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public CommentItemView(Context context) {
		super(context);
		initView();
	}
	private void initView(){
		LayoutInflater.from(getContext()).inflate(R.layout.listview_item_comment, this,true);
		rlLeft = (RelativeLayout)findViewById(R.id.RelativeLayout_left_comment);
		rlRight = (RelativeLayout)findViewById(R.id.RelativeLayout_right_comment);
		
		avatarLeft = (ImageView) findViewById(R.id.imageView_avatar_left);
		commentLeft = (TextView) findViewById(R.id.textView_comment_left);
		avatarRight = (ImageView) findViewById(R.id.imageView_avatar_right);
		commentRight = (TextView) findViewById(R.id.textView_comment_right);
		
		
	}
	public void setData(ChatItem chatItem){
		this.chatItem = chatItem;
		initData();
		//this.commentRight.setText(comment);
	}
	private void initData(){
		userId = chatItem.user+"";
		if(userId.equals(AppData.getUser(getContext())._id)){
			rlLeft.setVisibility(View.GONE);
			commentRight.setText(chatItem.msg);
			avatarRight.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					UISkip.skipToUserInfoActivity((Activity)getContext());
				}
			});
		}else{
			rlRight.setVisibility(View.GONE);
			commentLeft.setText(chatItem.msg);
			avatarLeft.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AppData.OTHER_USER = AppData.getUser(userId);
					UISkip.skip(false, getContext(), OtherInfoActivity.class);
				}
			});
		}
	}
}
