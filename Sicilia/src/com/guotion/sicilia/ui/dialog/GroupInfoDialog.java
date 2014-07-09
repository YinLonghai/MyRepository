package com.guotion.sicilia.ui.dialog;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.ui.dialog.GroupInfoEditDialog;
import com.guotion.sicilia.ui.dialog.GroupInfoEditDialog.OnGroupInfoEditListener;
import com.guotion.sicilia.ui.listener.OnUpdateListener;
import com.guotion.sicilia.util.PreferencesHelper;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @function 进入编界面辑群消息
 *
 * @author   Longhai.Yin  MailTo: 1195219040 @ qq.com
 *
 * @version  NO.01
 *
 * @create   2014-4-13 下午4:32:26
 *
 */
public class GroupInfoDialog extends Dialog implements View.OnClickListener{

	private Context context;

	private LinearLayout llBack;
	/**
	 * 返回
	 */
	private TextView returnGroup;
	/**
	 * 编辑
	 */
	private TextView tvEdit;
	/**
	 * 群头像
	 */
	private ImageView groupHead;
	/**
	 * 群名称
	 */
	private TextView groupName;
	/**
	 * 群备注
	 */
	private EditText etDesc;
	
	private RelativeLayout top;
	
	private ImageView ivBack;
	
	private ChatGroup chatGroup;
	private Resources resources = null;
	private GroupInfoEditDialog groupInfoEditIntoDialog = null;
	
	private OnUpdateListener<ChatGroup> onUpdateChatGroupListener = null;
	
	
	public GroupInfoDialog(Context context, ChatGroup chatGroup) {
		super(context, R.style.dialog_full_screen);
		this.context = context;
		resources = context.getResources();
		setContentView(R.layout.dialog_gropinfo);
		initView();
		setChatGroup(chatGroup);
		initListener();
	}

	public void setOnUpdateListener(OnUpdateListener<ChatGroup> l){
		this.onUpdateChatGroupListener = l;
	}
	
	public void setChatGroup(ChatGroup chatGroup){
		this.chatGroup = chatGroup;
		returnGroup.setText(chatGroup.GroupName);
		groupName.setText(chatGroup.GroupName);
		if(chatGroup.GroupProfile != null){
			etDesc.setText(chatGroup.GroupProfile);
		}				
		String imgUrl = chatGroup.GroupPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
			if(bitmap == null){
				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+chatGroup.GroupPhoto,groupHead,R.drawable.head_team,R.drawable.head_team);
			}else{
				groupHead.setImageBitmap(bitmap);
			}
		}else{
			groupHead.setImageResource(R.drawable.head_team);
		}		
	}
	
	private void initView() {
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		returnGroup = (TextView) findViewById(R.id.txt_return_group);
		tvEdit = (TextView) findViewById(R.id.textView_group_info);
		groupHead = (ImageView) findViewById(R.id.btn_group_head);
		groupName = (TextView) findViewById(R.id.tv_group_name);
		etDesc = (EditText) findViewById(R.id.edt_info);
		top =  (RelativeLayout) findViewById(R.id.relout_groupinfo_title);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		updateTheme();
	}
	
	private void updateTheme(){
		int theme = new PreferencesHelper(getContext()).getInt(AppData.THEME);
		int color ;
		try{
			switch(theme){
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				tvEdit.setTextColor(resources.getColor(R.color.white));
				returnGroup.setTextColor(resources.getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = resources.getColor(AppData.getThemeColor(theme));
				returnGroup.setTextColor(color);
				tvEdit.setTextColor(color);
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = resources.getColor(AppData.getThemeColor(theme));
				returnGroup.setTextColor(color);
				tvEdit.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				returnGroup.setTextColor(resources.getColor(R.color.white));
				tvEdit.setTextColor(resources.getColor(R.color.white));
				break;
			}
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void initListener() {
		llBack.setOnClickListener(this);
		tvEdit.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if (v == llBack) {
			dismiss();
		}else if (v == tvEdit) {
			if (groupInfoEditIntoDialog == null) {
				groupInfoEditIntoDialog = new GroupInfoEditDialog(context, chatGroup);
				groupInfoEditIntoDialog.setGroupInfoEditListener(new OnGroupInfoEditListener() {
					@Override
					public void getChatGroup(ChatGroup newChatGroup) {
						setChatGroup(newChatGroup);
						AppData.chatGroupList.remove(chatGroup);
						chatGroup = newChatGroup;
						AppData.chatGroupList.add(chatGroup);
						AppData.tempChatGroup = chatGroup;						
						if (onUpdateChatGroupListener != null) {
							onUpdateChatGroupListener.onUpdate(newChatGroup);
						}
					}
				});
			}
			groupInfoEditIntoDialog.setChatGroup(chatGroup);			
			groupInfoEditIntoDialog.show();
		}
	}

	
	public void notifyImageCreated() {
		groupInfoEditIntoDialog.notifyImageCreated();
	}
}
