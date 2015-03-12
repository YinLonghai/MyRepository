package com.guotion.sicilia.ui.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.ImgUtil;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.constants.AndroidRequestCode;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.ChatGroupManager;
import com.guotion.sicilia.ui.adapter.CommonPeopleAdapter;
import com.guotion.sicilia.ui.dialog.InviteMembersDialog.InviteMembersListener;
import com.guotion.sicilia.ui.popupwindow.ChoosePhotoPopupwindow;
import com.guotion.sicilia.ui.view.MeasureGridView;
import com.guotion.sicilia.util.AndroidFileUtils;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.StringUtils;

/**
 * @function 创建聊天组
 * 
 * @author Longhai.Yin MailTo: 1195219040 @ qq.com
 * 
 * @version NO.01
 * 
 * @create 2014-4-12 下午1:04:47
 * 
 */
public class CreateChatgroupDialog extends Dialog {

	private RelativeLayout rootView;

	private LinearLayout llBack;

	/**
	 * 返回对话
	 */
	private TextView returnChat;
	/**
	 * 提交
	 */
	private TextView submit;
	/**
	 * 群头像
	 */
	private ImageView groupHead;
	/**
	 * 群名称
	 */
	private EditText groupName;
	/**
	 * 邀请群成员
	 */
	private TextView inviteMember;
	/**
	 * 邀请的群成员信息
	 */
	private MeasureGridView memberInfo;

	private MeasureGridView gvPeople;
	private CommonPeopleAdapter commonPeopleAdapter = null;
	private ArrayList<User> userInfos;
	private Context context;
	private RelativeLayout top;
	private ImageView ivBack;
	private ChoosePhotoPopupwindow choosePhotoPopupwindow = null;
	private String imagePath = null;

	private final int CREAT_CHAT_GROUP_SUCESS = 1;

	private final int CREAT_CHAT_GROUP_ERROR = 2;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == CREAT_CHAT_GROUP_SUCESS) {
				if (createChatGroupListener != null) {
					createChatGroupListener.getChatGroup((ChatGroup) msg.obj);
					dismiss();
				}
			} else if (msg.what == CREAT_CHAT_GROUP_ERROR) {
				Toast.makeText(getContext(), "创建聊天组失败", Toast.LENGTH_SHORT).show();
			}

			super.handleMessage(msg);
		}
	};

	public CreateChatgroupDialog(Context context) {
		super(context, R.style.dialog_full_screen);
		setContentView(R.layout.activity_create_chatgroup);
		this.context = context;
		initData();
		initView(context);
		initListener();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	private void initData() {
		userInfos = new ArrayList<User>();

	}

	private void initView(Context context) {
		llBack = (LinearLayout) findViewById(R.id.LinearLayout_chat_back);
		rootView = (RelativeLayout) findViewById(R.id.relativeLayout_parent_view);
		returnChat = (TextView) findViewById(R.id.txt_return_chat);
		submit = (TextView) findViewById(R.id.txt_title_submit);
		groupHead = (ImageView) findViewById(R.id.img_group_head);
		groupName = (EditText) findViewById(R.id.txt_group_name);
		inviteMember = (TextView) findViewById(R.id.txt_invite_new_member);
		memberInfo = (MeasureGridView) findViewById(R.id.img_member_gridview);
		memberInfo.setSelector(new ColorDrawable(Color.TRANSPARENT)); // 设置点击时背景为透明色
		commonPeopleAdapter = new CommonPeopleAdapter(context, userInfos);
		memberInfo.setAdapter(commonPeopleAdapter);
		top = (RelativeLayout) findViewById(R.id.top_relout);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		updateTheme();
	}

	private void updateTheme() {
		int theme = new PreferencesHelper(getContext()).getInt(AppData.THEME);
		int color = 0;
		try {
			switch (theme) {
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				returnChat.setTextColor(getContext().getResources().getColor(R.color.white));
				submit.setTextColor(getContext().getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
				returnChat.setTextColor(color);
				submit.setTextColor(color);
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
				returnChat.setTextColor(color);
				submit.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				returnChat.setTextColor(getContext().getResources().getColor(R.color.white));
				submit.setTextColor(getContext().getResources().getColor(R.color.white));
				break;
			}
			if (color == 0) {
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
			}
			inviteMember.setTextColor(color);
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initListener() {
		memberInfo.setOnItemClickListener(new ItemClickListener());
		llBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		inviteMember.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppData.tempChatGroup = null;
				AppData.tempGroupMembers = getMembers();
				InviteMembersDialog inviteMembersDialog = new InviteMembersDialog(getContext());
				inviteMembersDialog.setInviteMembersListener(new InviteMembersListener() {
					@Override
					public void getMembers(List<User> list) {
						System.out.println("user list size-" + list.size());
						userInfos.addAll(list);
						commonPeopleAdapter.notifyDataSetChanged();
					}
				});
				inviteMembersDialog.show();
			}
		});
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = groupName.getText().toString();
				if (name == null || name.equals("")) {
					InputGroupNameDialog inputGroupNameDialog = new InputGroupNameDialog(getContext()) {
						@Override
						public void clickSure(String name) {
						}
					};
					inputGroupNameDialog.show();
				} else {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								StringBuilder sb = new StringBuilder(AppData.getUser(getContext())._id+"|");
								if (userInfos.size() > 0) {
									for (int i = 0; i < userInfos.size(); i++) {
										sb.append(userInfos.get(i)._id + "|");
									}
								}
								sb.deleteCharAt(sb.length() - 1);

								ChatGroup chatGroup = null;
								if (imagePath == null) {
									imagePath = CacheUtil.cloudImageCachePath+"groupHead.png";
									Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.head_team);
									CacheUtil.getInstence().cacheImage(bitmap, imagePath);
									
									chatGroup = new ChatGroupManager().createChatGroup(AppData.getUser(getContext())._id,
											sb.toString(), groupName.getText().toString(), ".png", new File(imagePath));
								} else {
									chatGroup = new ChatGroupManager().createChatGroup(AppData.getUser(getContext())._id,
											sb.toString(), groupName.getText().toString(), StringUtils.getFileSuffix(imagePath),
											new File(imagePath));
									imagePath = null;
								}
								if (chatGroup != null) {
									Message msg = new Message();
									msg.what = CREAT_CHAT_GROUP_SUCESS;
									msg.obj = chatGroup;
									handler.sendMessage(msg);
								} else {
									handler.sendEmptyMessage(CREAT_CHAT_GROUP_ERROR);
								}

							} catch (Exception e) {
								e.printStackTrace();
								// Message message = new Message();
								// String string = e.getMessage().replace(":",
								// "_");
								// message.obj =
								// getContext().getResources().getText(AppData.getStringResId(string));
								// handler.sendMessage(message);
								handler.sendEmptyMessage(CREAT_CHAT_GROUP_ERROR);
							}
						}
					}).start();
				}
			}
		});

		/**
		 * 选择群头像
		 */
		groupHead.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (choosePhotoPopupwindow == null) {
					choosePhotoPopupwindow = new ChoosePhotoPopupwindow((Activity) context);
				}
				choosePhotoPopupwindow.setPictureRequestCode(AndroidRequestCode.REQ_CODE_UPLOAD_GROUP_PICTURE);
				choosePhotoPopupwindow.setCameraRequestCode(AndroidRequestCode.REQ_CODE_UPLOAD_GROUP_CAMERA);
				choosePhotoPopupwindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
			}
		});
	}

	private List<String> getMembers() {
		List<String> list = new LinkedList<String>();
		for (User user : userInfos) {
			list.add(user._id);
		}
		return list;
	}

	/**
	 * 点击每一个邀请的新成员头像 产生相应的动作
	 * 
	 * @author Yu
	 * 
	 */
	private class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		}
	}

	private CreateChatGroupListener createChatGroupListener;

	public void setCreateChatGroupListener(CreateChatGroupListener createChatGroupListener) {
		this.createChatGroupListener = createChatGroupListener;
	}

	public interface CreateChatGroupListener {
		public void getChatGroup(ChatGroup chatGroup);
	}

	public void notifyImageCreated() {
		imagePath = choosePhotoPopupwindow.getImagePath();
		groupHead.setImageBitmap(ImgUtil.toRoundBitmap(AndroidFileUtils.getBitmap(imagePath, 360, 360)));
	}

}
