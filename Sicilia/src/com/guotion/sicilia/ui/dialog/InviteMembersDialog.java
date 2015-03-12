package com.guotion.sicilia.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.InviteMember;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.AccountManager;
import com.guotion.sicilia.im.util.ChatGroupManager;
import com.guotion.sicilia.ui.GroupMemberManageActivity;
import com.guotion.sicilia.ui.adapter.InviteMemberAdapter;
import com.guotion.sicilia.util.PreferencesHelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @function 邀请成员
 *
 * @author   Longhai.Yin  MailTo: 1195219040 @ qq.com
 *
 * @version  NO.01
 *
 * @create   2014-4-12 下午2:50:15
 *
 */
public class InviteMembersDialog extends Dialog {
	
	private LinearLayout llBack;

	/**
	 * 返回创建聊天组
	 */
	private TextView createChat;
	/**
	 * 添加成员
	 */
	private TextView addMember;
	/**
	 * 
	 */
	private ListView lvMain;
	
	private ArrayList<User> lvData;
	
	private InviteMemberAdapter lvAdapter;
	
	RelativeLayout top;
	ImageView ivBack;
	
	public InviteMembersDialog(Context context) {
		super(context,R.style.dialog_full_screen);
		setContentView(R.layout.activity_invite_member);
		initView();
		initData();
		initListener();
	}
	
	private void initData() {
		lvData = new ArrayList<User>();
		if(AppData.tempChatGroup == null){
			handleList();
		}else{
			getSingleChatGroup();
		}
		lvAdapter = new InviteMemberAdapter(getContext(), lvData);
		lvMain.setAdapter(lvAdapter);
		
	}

	private void initView() {
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		createChat = (TextView) findViewById(R.id.txt_create_chat);
		addMember = (TextView) findViewById(R.id.txt_add_menber);
		lvMain = (ListView) findViewById(R.id.lv_member_main);
		top =  (RelativeLayout) findViewById(R.id.relout_invite_title);
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
				createChat.setTextColor(getContext().getResources().getColor(R.color.white));
				addMember.setTextColor(getContext().getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
				createChat.setTextColor(color);
				addMember.setTextColor(color);
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
				createChat.setTextColor(color);
				addMember.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				createChat.setTextColor(getContext().getResources().getColor(R.color.white));
				addMember.setTextColor(getContext().getResources().getColor(R.color.white));
				break;
			}
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initListener() {
		llBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		lvMain.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
				    int position, long id) {
				
			}
		});
		addMember.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<User> list = lvAdapter.getChoose();
//				StringBuilder sb = new StringBuilder();
//				for(int i=0;i<list.size();i++){
//					sb.append(list.get(i)._id+"|");
//				}
//				sb.deleteCharAt(sb.length()-1);
				if(inviteMembersListener != null){
					inviteMembersListener.getMembers(list);
				}
				dismiss();
			}
		});
	}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				lvAdapter.setSize(lvData.size());
				lvAdapter.notifyDataSetChanged();
				break;
			case 2:
				Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	private void getUserList(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<User> list = new AccountManager().getUserList();
					lvData.addAll(list);
					AppData.userList.addAll(list);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					handler.sendEmptyMessage(2);
					e.printStackTrace();
				}
			}
		}).start();
	}
	private void getSingleChatGroup(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ChatGroup chatGroup = new ChatGroupManager().getSingleChatGroup(AppData.tempChatGroup._id);
					handleChatGroup(chatGroup);
					handler.sendEmptyMessage(1);
					//AppData.tempChatGroup = chatGroup;
				} catch (Exception e) {
					handler.sendEmptyMessage(2);
					e.printStackTrace();
				}
			}
		}).start();
	}
	private void handleChatGroup(ChatGroup chatGroup){
		Gson gson = new Gson();
		ArrayList<User> tempGroupMembers = new ArrayList<User>();
		User creator = gson.fromJson(chatGroup.creator+"", User.class);//System.out.println("creator="+creator);
		if(creator != null){
			tempGroupMembers.add(creator);
		}
		List<User> adminlist = gson.fromJson(chatGroup.admins+"",new TypeToken<List<User>>(){}.getType());//System.out.println("adminlist="+adminlist);
		if(adminlist != null){
			tempGroupMembers.addAll(adminlist);
		}
		List<User> members = gson.fromJson(chatGroup.members+"",new TypeToken<List<User>>(){}.getType());//System.out.println("members="+members);
		if(members != null){
			tempGroupMembers.addAll(members);
		}
		for(User user : AppData.userList){
			boolean b = false;
			for(User u : tempGroupMembers){
				if(user._id.equals(u._id)){//System.out.println(user.userName);
					b = true;
					break;
				}
			}
			if(!b){
				lvData.add(user);
			}
		}
	}
	private void handleList(){System.out.println(AppData.tempGroupMembers.size());
		for(User user : AppData.userList){
			boolean b = false;
			for(String uId : AppData.tempGroupMembers){
				if(user._id.equals(uId)){System.out.println(user.userName);
					b = true;
					break;
				}
			}
			if(!b){
				lvData.add(user);
			}
		}
	}
	
	private InviteMembersListener inviteMembersListener;
	
	public void setInviteMembersListener(InviteMembersListener inviteMembersListener) {
		this.inviteMembersListener = inviteMembersListener;
	}

	public interface InviteMembersListener{
		public void getMembers(List<User> list);
	}
}
