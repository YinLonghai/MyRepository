package com.guotion.sicilia.ui.view;

import com.guotion.common.PictureBrowser.SimpleNetImageView;
import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.common.utils.MyUtil;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.ConversationInfo;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.TextBold;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConversationItemView extends LinearLayout{
	private final int UPDATE_IVAVATAR = 1;

	private SimpleNetImageView ivAvatar;// 头像
	private TextView tvNewInfor;// 新消息标记
	private TextView tvContact;// 联系人名字
	private TextView tvContent;// 信息内容
	
	//private VolleyUtil volleyUtil;
	private ConversationInfo conversationInfo;
	private Context context;
	
	//private FriendDao friendDao ;
	//private String imgPath;
	
//	Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case UPDATE_IVAVATAR:
//				//ivAvatar.setImageBitmap(LocalImageCache.get().getCache(imgPath));
//				break;
//			}
//		}
//	};
	
	public ConversationItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		//initData();
		initView(context);
		initListener();
	}

	public ConversationItemView(Context context) {
		super(context);
		this.context = context;
		//initData();
		initView(context);
		initListener();
	}
	private void initView(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.listview_main_conversation_item, this,true);
		ivAvatar = (SimpleNetImageView) view.findViewById(R.id.ImageView_infor_avatar);
		tvNewInfor = (TextView) view.findViewById(R.id.TextView_newinfor);
		tvContact = (TextView) view.findViewById(R.id.TextView_contact);
		tvContent = (TextView) view.findViewById(R.id.TextView_content);
		TextBold.setTextBold(tvContact);//中文字体加粗
		TextBold.setTextBold(tvContent);
		
		int theme = new PreferencesHelper(context).getInt(AppData.THEME);
		try {
			tvNewInfor.setBackgroundResource(AppData.getThemeImgResId(theme, "textview_bg_corners"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initData() {
		//initNetImg();
//		friendDao = new FriendDao(AppDatas.gouxinDBHelper);
//		FriendInfo friendInfo = conversationInfo.friend;
//		friendDao.refresh(friendInfo);
		//volleyUtil = new VolleyUtil(context);
		//imgPath = conversationInfo.friend.userInfo.thumbnaiPath;
//		String imgPath = null;
//		if(imgPath==null){
//			//还没有上传头像，就用默认头像
//			ivAvatar.setImageResource(R.drawable.head_orang);
//		}else{
//			if(CacheUtil.getInstence().iSCacheFileExists(imgPath)){
//				//ivAvatar.setImageBitmap(LocalImageCache.get().loadImageBitmap(context, imgPath));
//			}else{
//				downloadImg();
//			}
//		}
		//tvNewInfor.setText(conversationInfo.unread_num+"");
		setNewInfor(conversationInfo.unread_num);
//		if(conversationInfo.friend != null)
//			tvContact.setText(conversationInfo.friend.remarkName);
//		if(conversationInfo.content != null)
//			tvContent.setText(ExpressionUtil.dealExpression(context, conversationInfo.content));
		tvContact.setText(conversationInfo.friendName);
		if(MyUtil.isAudioFile(conversationInfo.contentType)){
			tvContent.setText("语音");
		}else if(MyUtil.isImgFile(conversationInfo.contentType)){
			tvContent.setText("图片");
		}else{
			tvContent.setText(conversationInfo.content);
		}
		String imgUrl = conversationInfo.GroupPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			String cachePath = CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/"));
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(cachePath);
			if(bitmap != null){
				ivAvatar.setImageBitmap(bitmap);
			}
		}else{
			ivAvatar.setImageResource(R.drawable.head_orang);
		}
	}
	public void initNetImg(){
		String imgUrl = conversationInfo.GroupPhoto;
		if(imgUrl != null && !imgUrl.equals("")){//System.out.println(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
			String cachePath = CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/"));
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(cachePath);
			if(bitmap == null){
//				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+conversationInfo.GroupPhoto, ivAvatar, R.drawable.head_orang, R.drawable.head_orang);
				ivAvatar.loadImage(ChatServerConstant.URL.SERVER_HOST+conversationInfo.GroupPhoto, cachePath, R.drawable.head_orang);
			}else{
				ivAvatar.setImageBitmap(bitmap);
			}
		}else{
			ivAvatar.setImageResource(R.drawable.head_orang);
		}
	}
	private void initListener() {
		// TODO Auto-generated method stub
		
	}

	public void setConversationInfo(ConversationInfo conversationInfo) {
		this.conversationInfo = conversationInfo;
		initData();
	}
//	private void downloadImg(){
//		//volleyUtil.loadImageByVolley(conversationInfo.friend.userInfo.headPhotoUrl, ivAvatar, R.drawable.abc_ab_bottom_solid_dark_holo, R.drawable.abc_ab_bottom_solid_dark_holo);
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				//Bitmap bitmap = QQUtil.getbitmap(conversationInfo.friend.userInfo.thumbnailUrl);
//				//if(bitmap == null) return ;
//				//LocalImageCache.get().putCache(imgPath, bitmap);
//				handler.sendEmptyMessage(UPDATE_IVAVATAR);
//			}
//		}).start();
//	}
	private void setNewInfor(int num){
		if(num>0){
			tvNewInfor.setVisibility(View.VISIBLE);
			tvNewInfor.setText(num+"");
		}
		else
			tvNewInfor.setVisibility(View.GONE);
	}
	public void updataNewInfor(){
		if(conversationInfo.unread_num == 0) return ;
		conversationInfo.unread_num = 0;
		setNewInfor(0);
		//new ConversationDao(AppDatas.gouxinDBHelper).updateConversationInfo(conversationInfo);
	}
	public void deleteData(){
//		ChatMessageDao chatMessageDao = new ChatMessageDao(AppDatas.gouxinDBHelper);
//		ChatMessagePartDao chatMessagePartDao = new ChatMessagePartDao(AppDatas.gouxinDBHelper);
//		List<ChatMessageInfo> chatMessageList = chatMessageDao.getChatMessageInfos(conversationInfo.id);
//		for(ChatMessageInfo chatMessageInfo : chatMessageList){
//			chatMessagePartDao.deleteChatMessagePartBychatMessageId(chatMessageInfo.id);
//			chatMessageDao.deleteChatMessageInfo(chatMessageInfo.id);
//		}
//		new ConversationDao(AppDatas.gouxinDBHelper).deleteConversationInfo(conversationInfo.id);
	}
}
