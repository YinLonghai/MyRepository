package com.guotion.sicilia.ui.dialog;

import java.io.File;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.ImgUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.constants.AndroidRequestCode;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.im.util.ChatGroupManager;
import com.guotion.sicilia.ui.popupwindow.ChoosePhotoPopupwindow;
import com.guotion.sicilia.util.AndroidFileUtils;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.StringUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
public class GroupInfoEditDialog extends Dialog implements View.OnClickListener{

	/**
	 * 更新群组信息成功
	 */
	private final int UPDATE_CHAT_GROUP_SUCESS = 1;
	
	
	/**
	 * 更新群组失败
	 */
	private final int UPDATE_CHAT_GROUP_ERROR = 3;
	
	private LinearLayout llBack;
	/**
	 * 返回
	 */
	private TextView returnGroup;
	/**
	 * 提交
	 */
	private TextView submitGroup;
	/**
	 * 群头像
	 */
	private ImageView groupHead;
	/**
	 * 群名称
	 */
	private EditText groupName;
	/**
	 * 群备注
	 */
	private EditText editInfo;
	/**
	 * 提交提示信息
	 */
	private TextView submitRemind;
	
	private RelativeLayout top;
	private ImageView ivBack;
	
	private ChatGroup chatGroup;
	
	private OnGroupInfoEditListener groupInfoEditListener;
	
	private ChoosePhotoPopupwindow choosePhotoPopupwindow = null;
	
	private RelativeLayout rootLayout = null;
	private Context context = null;
	private String imagePath = null;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case UPDATE_CHAT_GROUP_SUCESS:
				setChatGroup(chatGroup);
				if (groupInfoEditListener != null) {
					groupInfoEditListener.getChatGroup(chatGroup);
				}
				dismiss();
				break;			
			case UPDATE_CHAT_GROUP_ERROR:
				Toast.makeText(getContext(), "群信息修改异常！", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
	public GroupInfoEditDialog(Context context, ChatGroup chatGroup) {
		super(context,R.style.dialog_full_screen);
		setContentView(R.layout.dialog_gropinfo_edit);
		this.context = context;
		initView();
		setChatGroup(chatGroup);
		initListener();
	}
	
	public void setChatGroup(ChatGroup chatGroup) {
		this.chatGroup = chatGroup;
		groupName.setText(chatGroup.GroupName);
		returnGroup.setText(chatGroup.GroupName);
		editInfo.setText(chatGroup.GroupProfile);
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
		rootLayout = (RelativeLayout)findViewById(R.id.relativeLayout_root);
		returnGroup = (TextView) findViewById(R.id.txt_return_group);
		submitGroup = (TextView) findViewById(R.id.edit_group_info);
		groupHead = (ImageView) findViewById(R.id.btn_group_head);
		groupName = (EditText) findViewById(R.id.tv_group_name);
		editInfo = (EditText) findViewById(R.id.edt_info);
		submitRemind = (TextView) findViewById(R.id.submit_remind);
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
				returnGroup.setTextColor(getContext().getResources().getColor(R.color.white));
				submitGroup.setTextColor(getContext().getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
				returnGroup.setTextColor(color);
				submitGroup.setTextColor(color);
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
				returnGroup.setTextColor(color);
				submitGroup.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				returnGroup.setTextColor(getContext().getResources().getColor(R.color.white));
				submitGroup.setTextColor(getContext().getResources().getColor(R.color.white));
				break;
			}
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initListener() {
		llBack.setOnClickListener(this);
		returnGroup.setOnClickListener(this);
		submitGroup.setOnClickListener(this);
		groupHead.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if (llBack == v) {
			imagePath = null;
			dismiss();
		}else if(returnGroup == v){ // 返回
			imagePath = null;
			dismiss();
		}else if(submitGroup == v){ // 提交
			submit();
		}else if(groupHead == v){// 点击头像
			if (choosePhotoPopupwindow == null) {
				choosePhotoPopupwindow = new ChoosePhotoPopupwindow((Activity)context);
			}
			choosePhotoPopupwindow.setPictureRequestCode(AndroidRequestCode.REQ_CODE_UPLOAD_GROUP_PICTURE);
			choosePhotoPopupwindow.setCameraRequestCode(AndroidRequestCode.REQ_CODE_UPLOAD_GROUP_CAMERA);
			choosePhotoPopupwindow.showAtLocation(rootLayout, Gravity.BOTTOM, 0, 0);
		}
	}
	
	
	private void submit() {
		submitRemind.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String name = groupName.getText().toString();
				String desc = editInfo.getText().toString();
				try {
					ChatGroup newChatGroup = null;
					if (imagePath == null) {
						newChatGroup = new ChatGroupManager().updateChatGroup(chatGroup._id, name, desc);
						
					}else {
						newChatGroup = new ChatGroupManager().updateChatGroup(chatGroup._id, name, desc, StringUtils.getFileSuffix(imagePath), new File(imagePath));
						imagePath = null;
					}
					if (newChatGroup != null) {
						chatGroup = newChatGroup;
						handler.sendEmptyMessage(UPDATE_CHAT_GROUP_SUCESS);
					}else {
						handler.sendEmptyMessage(UPDATE_CHAT_GROUP_ERROR);
					}
					
				} catch (Exception e) {
					handler.sendEmptyMessage(UPDATE_CHAT_GROUP_ERROR);
				}
			}
		}).start();
	}
	

	

	public void setGroupInfoEditListener(OnGroupInfoEditListener l) {
		this.groupInfoEditListener = l;
	}

	public interface OnGroupInfoEditListener{
		public void getChatGroup(ChatGroup chatGroup);
	}

	public void notifyImageCreated() {
		imagePath = choosePhotoPopupwindow.getImagePath();
		groupHead.setImageBitmap(ImgUtil.toRoundBitmap(AndroidFileUtils.getBitmap(imagePath, 320, 320)));
	}

	
}
