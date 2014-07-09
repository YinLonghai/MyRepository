package com.guotion.sicilia.ui.fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.AccountManager;
import com.guotion.sicilia.im.util.ChatGroupManager;
import com.guotion.sicilia.ui.ChatActivity;
import com.guotion.sicilia.ui.GroupChatActivity;
import com.guotion.sicilia.ui.adapter.ContactsAdapter;
import com.guotion.sicilia.ui.adapter.GroupMemberAdapter;
import com.guotion.sicilia.ui.dialog.CreateChatgroupDialog;
import com.guotion.sicilia.ui.dialog.CreateChatgroupDialog.CreateChatGroupListener;
import com.guotion.sicilia.ui.view.ContastItemView;
import com.guotion.sicilia.ui.view.GroupItemView;
import com.guotion.sicilia.ui.view.PullDownView;
import com.guotion.sicilia.ui.view.PullDownView.OnPullDownListener;
import com.guotion.sicilia.ui.view.ScrollOverListView.ScrollListener;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.UISkip;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RadioGroup.OnCheckedChangeListener;
/**
 * @function 联系人or聊天组
 *
 * @author   Longhai.Yin  MailTo: 1195219040 @ qq.com
 *
 * @version  NO.01
 *
 * @create   2014-4-9 下午2:46:10
 *
 */
public class MemberFragment extends Fragment {

	private View contentView;
	/**
	 * listview上方的居中的布局id
	 */
	private LinearLayout linloutTitle;
	/**
	 * 居中的布局的单选父控件
	 */
	private RadioGroup radioGroup;

	/**
	 * 右侧添加更多图片button     聊天组
	 */
	private ImageView addOff;
	/**
	 * 搜索的内容
	 */
	private EditText searchContent;
	
	/**
	 * 聊天组的listview
	 */
	private ListView lvMain;
	/**
	 * 聊天组item实体
	 */
	private List<ChatGroup> lvData ;
	/**
	 * 聊天组adapter
	 */
	private GroupMemberAdapter lvAdapter;
	/**
	 * 联系人的listview
	 */
	private ListView lvMainContacts;
	/**
	 * 联系人的item实体
	 */
	private List<User> lvDataContacts ;
	/**
	 * 联系人adapter
	 */
	private ContactsAdapter lvAdapterContacts;
	
	private RelativeLayout top;
	private int theme;
	
	private int memberImgResId;
	private int groupImgResId;
	private PullDownView contactsPullDownView, groupPullDownView;
	private OnPullDownListener pullDownListener = null;
	private List<User> searchUsers = null;
	private List<ChatGroup> searchChatGroups = null;
	
	private CreateChatgroupDialog createChatgroupDialog = null;
	
	private boolean isUser = true;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:System.out.println("user list"+lvDataContacts.size());
				lvAdapterContacts.notifyDataSetChanged();
				break;
			case 3:System.out.println("group list"+lvData.size());
				lvAdapter.notifyDataSetChanged();
				break;
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView = inflater.inflate(R.layout.fragment_group_member, container,false);
		initView();
		initData();
		initListener();
		return contentView;
	}
	@Override
	public void onResume() {
		int theme = new PreferencesHelper(getActivity()).getInt(AppData.THEME);//System.out.println("MemberFragment===old-"+this.theme+"new-"+theme);
		if(this.theme != theme){
			this.theme = theme;
			updateTheme();
		}
		super.onResume();
	}

	public void notifyImageCreated(){
		createChatgroupDialog.notifyImageCreated();
	}
	private void initData() {
		lvData = new ArrayList<ChatGroup>();
		lvDataContacts = new ArrayList<User>();
		
		searchUsers = new LinkedList<User>();
		searchChatGroups = new LinkedList<ChatGroup>();
		
		lvAdapterContacts = new ContactsAdapter(getActivity(), searchUsers);
		lvAdapter = new GroupMemberAdapter(getActivity(), searchChatGroups);
		lvMainContacts.setAdapter(lvAdapterContacts);
		lvMain.setAdapter(lvAdapter);
		if(AppData.userList.size() == 0){
			getUserList();
		}else if(lvDataContacts.size() == 0){
			lvDataContacts.addAll(AppData.userList);
			searchUsers.addAll(AppData.userList);
			lvAdapterContacts.notifyDataSetChanged();
		}
		if(AppData.chatGroupList.size() == 0){
			getUserRelateGroups();
		}else if(lvData.size() == 0){
			lvData.addAll(AppData.chatGroupList);
			searchChatGroups.addAll(AppData.chatGroupList);
			lvAdapter.notifyDataSetChanged();
		}
	}


	private void initView() {
		theme = new PreferencesHelper(getActivity()).getInt(AppData.THEME);//System.out.println("theme-"+theme);
		linloutTitle = (LinearLayout) contentView.findViewById(R.id.Linlout_title);
		radioGroup = (RadioGroup) contentView.findViewById(R.id.radio_group01);
		
		addOff = (ImageView) contentView.findViewById(R.id.add_off_img);

		groupPullDownView = (PullDownView)contentView.findViewById(R.id.lv_main);
		contactsPullDownView = (PullDownView)contentView.findViewById(R.id.lv_main_contacts);
		//显示并且可以使用头部刷新
		groupPullDownView.setShowHeader();
		contactsPullDownView.setShowHeader();

		groupPullDownView.setTvFreshIsVisible(false);
		contactsPullDownView.setTvFreshIsVisible(false);
		
		lvMain = groupPullDownView.getListView();
		lvMainContacts = contactsPullDownView.getListView();
		
		searchContent = (EditText) contentView.findViewById(R.id.search_txt);
		top = (RelativeLayout) contentView.findViewById(R.id.Relout_title_bar);
		updateTheme();
	}
	private void updateTheme(){
		try{
			memberImgResId = AppData.getThemeImgResId(theme, "contract");//System.out.println(memberImgResId);
			groupImgResId = AppData.getThemeImgResId(theme, "chat_groups");System.out.println(theme);
			switch(theme){
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				break;
			case AppData.THEME_RED:System.out.println("THEME_RED");
			top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				break;
			case AppData.THEME_FEMALE:System.out.println("THEME_FEMALE");
				top.setBackgroundResource(AppData.getThemeColor(theme));
				break;
			}
			addOff.setImageResource(AppData.getThemeImgResId(theme, "add"));
			linloutTitle.setBackgroundResource(memberImgResId);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initListener() {
		addOff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createChatgroupDialog = new CreateChatgroupDialog(getActivity());
				createChatgroupDialog.setCreateChatGroupListener(new CreateChatGroupListener() {
					@Override
					public void getChatGroup(ChatGroup chatGroup) {
						lvData.add(0, chatGroup);
						searchChatGroups.add(0, chatGroup);
						lvAdapter.notifyDataSetChanged();
						AppData.chatGroupList.add(0, chatGroup);
					}
				});
				createChatgroupDialog.show();
			}
		});
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.btn_contract) { // 联系人
					isUser = true;
					addOff.setVisibility(View.GONE);
					groupPullDownView.setVisibility(View.GONE);
					contactsPullDownView.setVisibility(View.VISIBLE);
					linloutTitle.setBackgroundResource(memberImgResId);
				} else {  // 聊天组
					isUser = false;
					addOff.setVisibility(View.VISIBLE);
					contactsPullDownView.setVisibility(View.GONE);
					groupPullDownView.setVisibility(View.VISIBLE);
					linloutTitle.setBackgroundResource(groupImgResId);
				}
			}
		});
		lvMain.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				AppData.tempChatGroup = searchChatGroups.get(arg2-1);
				UISkip.skip(false, getActivity(), GroupChatActivity.class);
			}
		});
		lvMainContacts.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				AppData.tempuser = searchUsers.get(arg2-1);
				UISkip.skip(false, getActivity(), ChatActivity.class);
			}
		});
		
		contactsPullDownView.setOnPullDownListener(new OnPullDownListener() {
			
			@Override
			public void onRefresh() {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						/** 关闭 刷新完毕 ***/
						contactsPullDownView.RefreshComplete();//这个事线程安全的 可看源代码
					}
				}).start();			
			}
		});
		groupPullDownView.setOnPullDownListener(new OnPullDownListener() {
			
			@Override
			public void onRefresh() {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						/** 关闭 刷新完毕 ***/
						groupPullDownView.RefreshComplete();//这个事线程安全的 可看源代码
					}
				}).start();			
			}
		});
		
		searchContent.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					if (isUser) {
						searchUser(s.toString());
					}else {
						searchChatGroup(s.toString());
					}
				}else {
					searchUsers.clear();
					searchChatGroups.clear();
					searchUsers.addAll(lvDataContacts);
					searchChatGroups.addAll(lvData);
					lvAdapter.notifyDataSetChanged();
					lvAdapterContacts.notifyDataSetChanged();
				}
			}
		});
		
		contactsPullDownView.setScrollListener(new ScrollListener() {
			boolean isFirst = true;
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					for(int i=0;i<view.getChildCount();i++){
						View childView = view.getChildAt(i);
						if(childView instanceof ContastItemView){
							ContastItemView contastItemView = (ContastItemView) childView;
							contastItemView.initNetImg();
						}
					}
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisiableItem, int arg2,
					int arg3) {
				if(!isFirst) return;
				if(view.getChildCount()>0){
					for(int i=0;i<view.getChildCount();i++){
						View childView = view.getChildAt(i);
						if(childView instanceof ContastItemView){
							ContastItemView contastItemView = (ContastItemView) childView;
							contastItemView.initNetImg();
							isFirst = false;
						}
					}
				}
			}
		});
		groupPullDownView.setScrollListener(new ScrollListener() {
			boolean isFirst = true;
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					for(int i=0;i<view.getChildCount();i++){
						View childView = view.getChildAt(i);
						if(childView instanceof GroupItemView){
							GroupItemView groupItemView = (GroupItemView) childView;
							groupItemView.initNetImg();
						}
					}
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisiableItem, int arg2,
					int arg3) {
				if(!isFirst) return;
				if(view.getChildCount()>0){
					for(int i=0;i<view.getChildCount();i++){
						View childView = view.getChildAt(i);
						if(childView instanceof GroupItemView){
							GroupItemView groupItemView = (GroupItemView) childView;
							groupItemView.initNetImg();
							isFirst = false;
						}
					}
				}
			}
		});
	}
	
	private void getUserList(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<User> list = new AccountManager().getUserList();System.out.println("user list"+list.size());
					lvDataContacts.addAll(list);
					searchUsers.addAll(list);
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

	private void getUserRelateGroups(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<ChatGroup> list = new ChatGroupManager().getUserRelateGroups(AppData.getUser(getActivity())._id);System.out.println("group list"+list.size());
					for(ChatGroup chatGroup : list){
						if(chatGroup.p2pid == null || chatGroup.p2pid.equals("")){
							lvData.add(chatGroup);
							searchChatGroups.add(chatGroup);
							AppData.chatGroupList.add(chatGroup);
						}
					}
					handler.sendEmptyMessage(3);
				} catch (Exception e) {
					handler.sendEmptyMessage(4);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void searchUser(String key){
		searchUsers.clear();
		for (User user:lvDataContacts) {
			if (user.nickName.toLowerCase().contains(key.toLowerCase())) {
				searchUsers.add(user);
			}
		}
		lvAdapterContacts.notifyDataSetChanged();
	}
	
	private void searchChatGroup(String key){
		searchChatGroups.clear();
		for (ChatGroup chatGroup:lvData) {
			if (chatGroup.GroupName.toLowerCase().contains(key.toLowerCase())) {
				searchChatGroups.add(chatGroup);
			}
		}
		lvAdapter.notifyDataSetChanged();
	}
}
