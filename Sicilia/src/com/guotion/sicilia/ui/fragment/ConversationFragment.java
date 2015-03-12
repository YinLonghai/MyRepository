package com.guotion.sicilia.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.ConversationInfo;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.bean.net.ChatHistory;
import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.AccountManager;
import com.guotion.sicilia.im.util.ChatGroupManager;
import com.guotion.sicilia.im.util.ChatItemManager;
import com.guotion.sicilia.im.util.ChatItemSorter;
import com.guotion.sicilia.im.util.OffineMessageManager;
import com.guotion.sicilia.ui.ChatActivity;
import com.guotion.sicilia.ui.GroupChatActivity;
import com.guotion.sicilia.ui.adapter.ConversationAdapter;
import com.guotion.sicilia.ui.dialog.CreateChatgroupDialog;
import com.guotion.sicilia.ui.dialog.CreateChatgroupDialog.CreateChatGroupListener;
import com.guotion.sicilia.ui.dialog.WarningDialog;
import com.guotion.sicilia.ui.listener.ReceiveGroupMessageListener;
import com.guotion.sicilia.ui.listener.ReceiveP2PMessageListener;
import com.guotion.sicilia.ui.view.ConversationItemView;
import com.guotion.sicilia.ui.view.PullDownView;
import com.guotion.sicilia.ui.view.PullDownView.OnPullDownListener;
import com.guotion.sicilia.ui.view.ScrollOverListView.ScrollListener;
import com.guotion.sicilia.util.LogUtil;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.UISkip;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.RelativeLayout;

public class ConversationFragment extends Fragment {

	private View contentView;
	private ImageView add;
	private RelativeLayout top;
	private PullDownView pullDownView;
	private ListView lvInfor;
	private ConversationAdapter inforAdapter = null;
	private LinkedList<ConversationInfo> conversationList = null;
	private OnClickListener clickListener = null;
	private OnItemClickListener itemClickListener = null;
	private OnItemLongClickListener itemLongClickListener = null;
	private OnPullDownListener pullDownListener = null;

	private Gson gson = new Gson();
	private int theme;

	private HashMap<String, ChatGroup> groupMap = new HashMap<String, ChatGroup>();

	private ReceiveP2PMessageListenerImpl receiveP2PMessageListenerImpl;
	private ReceiveGroupMessageListenerImpl receiveGroupMessageListenerImpl;

	private boolean getConversationData = false;

	private final int USER_DONE = 2;
	private final int GROUP_DONE = 3;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (inforAdapter != null) {
					inforAdapter.notifyDataSetChanged();
				}
				getConversationData = false;
				break;
			case GROUP_DONE:
				break;
			case USER_DONE:
				// getUserRelateGroups();
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_main_conversation, container, false);
		initData();
		initView(contentView);
		initListener();
		return contentView;
	}

	@Override
	public void onResume() {
		int theme = new PreferencesHelper(getActivity()).getInt(AppData.THEME);
		if (this.theme != theme) {
			this.theme = theme;
			updateTheme();
		}
		conversationList.clear();
		conversationList.addAll(AppData.conversationList);
		handleTempChatItem();
		inforAdapter.notifyDataSetChanged();
		LogUtil.i("onResume Conversation Fragment");
		super.onResume();
	}

	private void handleTempChatItem() {System.out.println("handleTempChatItem");
		for (ChatItem chatItem : AppData.tempP2pChatList) {
			updateP2PConversation(chatItem);
		}
		for (ChatItem chatItem : AppData.tempGroupChatList) {
			updateGroupConversation(chatItem);
		}
	}

	@Override
	public void onStart() {
		LogUtil.i("onStart Conversation Fragment");
		super.onStart();
	}
//	@Override
//	public void onDestroyView() {
//		System.out.println("ConversationFragment onDestroyView");
//		AppData.imMessageToUIListener.cancleReceiveP2PMessageListeners(receiveP2PMessageListenerImpl);
//		AppData.imMessageToUIListener.cancleReceiveGroupMessageListeners(receiveGroupMessageListenerImpl);
//		super.onDestroyView();
//	}

	private void initData() {
		theme = new PreferencesHelper(getActivity()).getInt(AppData.THEME);
		conversationList = new LinkedList<ConversationInfo>();
		if(receiveP2PMessageListenerImpl == null){
			receiveP2PMessageListenerImpl = new ReceiveP2PMessageListenerImpl();
			AppData.imMessageToUIListener.registReceiveP2PMessageListeners(receiveP2PMessageListenerImpl);
		}
		if(receiveGroupMessageListenerImpl==null){
			receiveGroupMessageListenerImpl = new ReceiveGroupMessageListenerImpl();
			AppData.imMessageToUIListener.registReceiveGroupMessageListeners(receiveGroupMessageListenerImpl);
		}
	}

	private void initView(View contentView) {
		add = (ImageView) contentView.findViewById(R.id.imageView_add);
		top = (RelativeLayout) contentView.findViewById(R.id.relativeLayout1);
		pullDownView = (PullDownView) contentView.findViewById(R.id.ListView_infor);
		pullDownView.setTvFreshIsVisible(true);
		// 显示并且可以使用头部刷新
		pullDownView.setShowHeader();
		lvInfor = pullDownView.getListView();
		inforAdapter = new ConversationAdapter(getActivity(), conversationList);
		lvInfor.setAdapter(inforAdapter);
		// List<ChatGroup> list = gson.fromJson(AppData.USER.chatGroups+"",new
		// TypeToken<List<ChatGroup>>(){}.getType());
		// for(ChatGroup chatGroup : list){
		// getMsgRelateHistory(chatGroup._id);
		// }
		if (AppData.conversationList.size() == 0) {
			getConversationList_1();
		} else {
			conversationList.addAll(AppData.conversationList);
		}
		updateTheme();
	}

	private void updateTheme() {
		try {
			switch (theme) {
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				break;
			}
			add.setImageResource(AppData.getThemeImgResId(theme, "add"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initListener() {
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CreateChatgroupDialog createChatgroupDialog = new CreateChatgroupDialog(getActivity());
				createChatgroupDialog.setCreateChatGroupListener(new CreateChatGroupListener() {
					@Override
					public void getChatGroup(ChatGroup chatGroup) {
						groupMap.put(chatGroup._id, chatGroup);
						AppData.chatGroupList.add(chatGroup);
						ConversationInfo conversationInfo = new ConversationInfo();
						conversationInfo.groupId = chatGroup._id;
						conversationInfo.friendName = chatGroup.GroupName;
						conversationInfo.GroupPhoto = chatGroup.GroupPhoto;
						conversationList.add(0, conversationInfo);
						AppData.conversationList.add(conversationInfo);
						handler.sendEmptyMessage(1);
					}
				});
				createChatgroupDialog.show();
			}
		});
		clickListener = new ConversationClickListener();

		pullDownListener = new ConversationPullDownListener();
		pullDownView.setOnPullDownListener(pullDownListener);

		itemClickListener = new ConversationItemClickListener();
		lvInfor.setOnItemClickListener(itemClickListener);

		itemLongClickListener = new ConversationItemLongClickListener();

		lvInfor.setOnItemLongClickListener(itemLongClickListener);
		pullDownView.setScrollListener(new ScrollListener() {
			boolean isFirst = true;
			int childCount = 0;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 当不滚动时
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					for (int i = 0; i < view.getChildCount(); i++) {
						View childView = view.getChildAt(i);
						if (childView instanceof ConversationItemView) {
							ConversationItemView conversationItemView = (ConversationItemView) childView;
							conversationItemView.initNetImg();
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (view.getChildCount() > childCount) {
					childCount = view.getChildCount();
					isFirst = true;
				}
				if (!isFirst)
					return;
				if (view.getChildCount() > 0) {
					for (int i = 0; i < view.getChildCount(); i++) {
						View childView = view.getChildAt(i);
						if (childView instanceof ConversationItemView) {
							ConversationItemView conversationItemView = (ConversationItemView) childView;
							conversationItemView.initNetImg();
							isFirst = false;
						}
					}
				}
			}
		});

	}

	ChatItemManager chatItemManager = new ChatItemManager();

	// HashMap<String,List<ChatItem>> chatMap = new
	// HashMap<String,List<ChatItem>>();
	private void getMsgRelateHistory(final ChatGroup chatGroup) {
		System.out.println("getMsgRelateHistory");

		List<ChatItem> chatItemList;
		try {
			chatItemList = chatItemManager.getMsgListWithGroupId(chatGroup._id);
			ConversationInfo conversation = new ConversationInfo();
			if (chatItemList.size() > 0) {
				conversation.content = chatItemList.get(chatItemList.size() - 1).msg;
				conversation.contentType = chatItemList.get(chatItemList.size() - 1).mediaType;
				conversation.unread_num = chatItemList.size();
			} else
				return;
			if (chatGroup.p2pid != null && !chatGroup.p2pid.equals("")) {
				String userId = chatGroup.p2pid.substring(0, chatGroup.p2pid.indexOf("-"));
				User u = userMap.get(userId);
				if (u == null)
					return;
				conversation.friendName = u.userName;
				conversation.GroupPhoto = u.headPhoto;
				conversation.accountId = u._id;
				AppData.chatMap.put(u._id, chatItemList);
			} else {
				conversation.friendName = chatGroup.GroupName;
				conversation.GroupPhoto = chatGroup.GroupPhoto;
				conversation.groupId = chatGroup._id;
				AppData.chatMap.put(chatGroup._id, chatItemList);
			}
			conversationList.add(conversation);
			System.out.println("add  conversation");
			AppData.conversationList.add(conversation);
			handler.sendEmptyMessage(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private final class ConversationPullDownListener implements OnPullDownListener {

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
					pullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
				}
			}).start();
		}

	}// end of ConversationPullDownListener

	private final class ConversationClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
		}
	}// end of ConversationClickListener

	private final class ConversationItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ConversationItemView conversationItemView = (ConversationItemView) view;
			conversationItemView.updataNewInfor();
			ConversationInfo conversationInfo = conversationList.get(position - 1);
			if (conversationInfo.accountId != null && !conversationInfo.accountId.equals("")) {
				AppData.tempuser = userMap.get(conversationInfo.accountId);
				UISkip.skip(false, getActivity(), ChatActivity.class);
			} else if (conversationInfo.groupId != null && !conversationInfo.groupId.equals("")) {
				System.out.print("conversationInfo.groupId=="+conversationInfo.groupId);
				AppData.tempChatGroup = groupMap.get(conversationInfo.groupId);
				if(AppData.tempChatGroup==null){
					AppData.tempChatGroup = AppData.getChatGroup(conversationInfo.groupId);
				}
				System.out.println("AppData.tempChatGroup=="+AppData.tempChatGroup);
				UISkip.skip(false, getActivity(), GroupChatActivity.class);
			}

		}

	}// end of lvItemClickListener

	private final class ConversationItemLongClickListener implements OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View view, final int arg2, long arg3) {
			System.out.println("onItemLongClick==="+arg2);
			String warning = "您确实要删除该会话吗？";
			String cancel = "取消";
			String sure = "删除";
			WarningDialog warningDialog = new WarningDialog(getActivity(), warning, cancel, sure) {
				@Override
				public void clickCancel() {
				}

				@Override
				public void clickSure() {
					AppData.conversationList.remove(arg2-1);
					conversationList.remove(arg2-1);
					inforAdapter.notifyDataSetChanged();
				}
			};
			warningDialog.show();
			return false;
		}
	}// end of ConversationItemLongClickListener

	private final class ReceiveP2PMessageListenerImpl implements ReceiveP2PMessageListener {
		@Override
		public void receiveP2PMessage(ChatItem chatItem) {System.out.println("ReceiveP2PMessageListener in ConversationFragment....."+receiveP2PMessageListenerImpl);
			updateP2PConversation(chatItem);
			handler.sendEmptyMessage(1);
		}
	}

	private final class ReceiveGroupMessageListenerImpl implements ReceiveGroupMessageListener {
		@Override
		public void receiveGroupMessage(ChatItem chatItem) {System.out.println("ReceiveGroupMessageListener in ConversationFragment");
			updateGroupConversation(chatItem);
			handler.sendEmptyMessage(1);
		}
	}

	private void updateP2PConversation(ChatItem chatItem) {
		User user;
		if (chatItem.mediaType == null || chatItem.mediaType.equals("")) {
			user = gson.fromJson(chatItem.user + "", User.class);
		} else {
			user = AppData.getUser(chatItem.user + "");
		}
		chatItem.userInfo = user;
		ConversationInfo conversation = null;
		List<ChatItem> chatList;
		for (int i = 0; i < conversationList.size(); i++) {
			if (user._id.equals(conversationList.get(i).accountId)) {
				conversation = conversationList.get(i);
				conversation.content = chatItem.msg;
				conversation.contentType = chatItem.mediaType;
				conversation.unread_num += 1;
				conversationList.remove(i);
				break;
			}
		}
		if (conversation == null) {
			conversation = new ConversationInfo();
			conversation.accountId = user._id;
			conversation.content = chatItem.msg;
			conversation.contentType = chatItem.mediaType;
			conversation.friendName = user.userName;
			conversation.GroupPhoto = user.headPhoto;
			conversation.unread_num += 1;
			AppData.conversationList.add(0, conversation);
		}
		conversationList.add(0, conversation);
		chatList = AppData.chatMap.get(conversation.accountId);
		if (chatList == null) {
			chatList = new ArrayList<ChatItem>();
		}
		chatList.add(chatItem);
		AppData.chatMap.put(conversation.accountId, chatList);
	}

	private void updateGroupConversation(ChatItem chatItem) {
		User user;
		ChatGroup group;
		System.out.println("chatItem.chatGroup===" + chatItem.chatGroup);
		if (chatItem.mediaType == null || chatItem.mediaType.equals("")) {
			user = gson.fromJson(chatItem.user + "", User.class);

		} else {
			user = AppData.getUser(chatItem.user + "");
			// group = AppData.getChatGroup(chatItem.chatGroup+"");
		}
		group = gson.fromJson(chatItem.chatGroup + "", ChatGroup.class);
		chatItem.userInfo = user;
		ConversationInfo conversation = null;
		List<ChatItem> chatList;
		for (int i = 0; i < conversationList.size(); i++) {
			if (group._id.equals(conversationList.get(i).groupId)) {
				conversation = conversationList.get(i);
				conversation.content = chatItem.msg;
				conversation.contentType = chatItem.mediaType;
				conversation.unread_num += 1;
				conversationList.remove(i);
				break;
			}
		}
		if (conversation == null) {
			conversation = new ConversationInfo();
			conversation.groupId = group._id;
			conversation.content = chatItem.msg;
			conversation.contentType = chatItem.mediaType;
			conversation.friendName = group.GroupName;
			conversation.GroupPhoto = group.GroupPhoto;
			conversation.unread_num += 1;
			AppData.conversationList.add(0, conversation);
		}
		conversationList.add(0, conversation);
		chatList = AppData.chatMap.get(conversation.groupId);
		if (chatList == null) {
			chatList = new ArrayList<ChatItem>();
		}
		chatList.add(chatItem);
		AppData.chatMap.put(conversation.groupId, chatList);
	}

	private List<ChatGroup> getUserRelateGroups() {
		List<ChatGroup> relateList = new LinkedList<ChatGroup>();
		try {
			User user = AppData.getUser(getActivity());
			List<ChatGroup> list = new ChatGroupManager().getUserRelateGroups(user._id);
			for (ChatGroup chatGroup : list) {
				if (chatGroup.p2pid.endsWith(user._id) && !chatGroup.p2pid.startsWith(user._id)) {// &&
																									// !chatGroup.p2pid.startsWith(user._id)
					relateList.add(chatGroup);// System.out.println("RelateGroup p2pid = "+chatGroup.p2pid);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return relateList;
	}

	private List<ChatGroup> getUserP2PGroups() {
		List<ChatGroup> relateList = new LinkedList<ChatGroup>();
		User user = AppData.getUser(getActivity());
		try {
			List<ChatGroup> list = new ChatGroupManager().getUserRelateGroups(user._id);
			for (ChatGroup group : list) {
				if (group.p2pid.startsWith(user._id) && !group.p2pid.endsWith(user._id)) {
					relateList.add(group);
				}
				if (!group.p2pid.startsWith(user._id) && group.p2pid.endsWith(user._id)) {
					relateList.add(group);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return relateList;
	}

	private HashMap<String, User> userMap = new HashMap<String, User>();

	private void getConversationList_1() {
		if (getConversationData)
			return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					getConversationData = true;
					List<User> list = new AccountManager().getUserList();
					for (User user : list) {
						userMap.put(user._id, user);
					}
					ChatItemManager itemManager = new ChatItemManager();
					List<ChatGroup> chatGroups = new ArrayList<ChatGroup>();
					// 1.先获取所有的p2p组
					List<ChatGroup> p2pGroups = getUserP2PGroups();// getUserP2PGroups
					for (ChatGroup chatGroup : p2pGroups) {
						groupMap.put(chatGroup._id, chatGroup);
					}
					chatGroups.addAll(p2pGroups);
					// 2.获取普通组
					List<ChatGroup> commonGroups = gson.fromJson(AppData.getUser(getActivity()).chatGroups + "",
							new TypeToken<List<ChatGroup>>() {
							}.getType());
					for (ChatGroup chatGroup : commonGroups) {
						groupMap.put(chatGroup._id, chatGroup);
					}
					chatGroups.addAll(commonGroups);

					for (ChatGroup group : chatGroups) {
						ChatHistory chatHistory = null;
						try {
							chatHistory = itemManager.getMsgRelateHistory(group._id);
							List<ChatItem> oneGroupItems = new ArrayList<ChatItem>();
							List<ChatItem> chatItems = gson.fromJson(chatHistory.getChatItem() + "",
									new TypeToken<List<ChatItem>>() {
									}.getType());

							System.out.println(group._id + "的消息条数是:" + chatItems.size());

							oneGroupItems.addAll(chatItems);
							while (chatItems.size() >= 20) {
								chatItems = chatItemManager.getMsgAfterOneMsg(chatHistory._id,
										chatItems.get(chatItems.size() - 1)._id);
								oneGroupItems.addAll(chatItems);
							}
							System.out.println(group._id + "的总消息条数是:" + oneGroupItems.size());
							handleOffineMessage(group._id, oneGroupItems);
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("异常的groupId=" + group._id);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// handler.sendEmptyMessage(2);
					e.printStackTrace();
				}
				System.out.println("conversation load over...");
			}
		}).start();
	}

	private void getConversationList() {
		if (getConversationData)
			return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					getConversationData = true;
					List<User> list = new AccountManager().getUserList();
					for (User user : list) {
						userMap.put(user._id, user);
					}
					// handler.sendEmptyMessage(USER_DONE);
					List<ChatGroup> chatlist = new LinkedList<ChatGroup>();
					List<ChatGroup> list1 = gson.fromJson(AppData.getUser(getActivity()).chatGroups + "",
							new TypeToken<List<ChatGroup>>() {
							}.getType());
					chatlist.addAll(list1);
					chatlist.addAll(getUserRelateGroups());
					for (ChatGroup chatGroup : chatlist) {
						groupMap.put(chatGroup._id, chatGroup);
					}
					System.out.println("conversation load start...");
					// HashMap<String,String> offineGroupMap = new
					// HashMap<String,String>();
					// Map<String, Integer> offineMap = getOffineMessage();
					Map<String, List<ChatItem>> offineMap = offineMessageManager.getHaveLastReadOffineMessage(AppData
							.getUser(getActivity())._id);
					for (Entry<String, List<ChatItem>> entry : offineMap.entrySet()) {// System.out.println("aaa");
						chatlist.remove(groupMap.get(entry.getKey()));
						handleOffineMessage(entry.getKey(), entry.getValue());
					}
					for (ChatGroup chatGroup : chatlist) {
						getMsgRelateHistory(chatGroup);
					}
					// AppData.conversationList.addAll(conversationList);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// handler.sendEmptyMessage(2);
					e.printStackTrace();
				}
				System.out.println("conversation load over...");
			}
		}).start();
	}

	private void handleOffineMessage(String chatGroupId, List<ChatItem> chatItemList) {// System.out.println("bbb");
		ChatGroup chatGroup = groupMap.get(chatGroupId);
		if (chatGroup == null)
			return;
		if (chatItemList.size() == 0)
			return;
		ChatItem chatItem = chatItemList.get(chatItemList.size() - 1);
		ConversationInfo conversationInfo = null;
		int unread_num = getUnReadNum(chatItemList);
		if (unread_num == 0)
			return;
		if (chatGroup.p2pid != null && !chatGroup.p2pid.equals("")) {
			String[] ids = TextUtils.split(chatGroup.p2pid, "-");
			// String userId = chatGroup.p2pid.substring(0,
			// chatGroup.p2pid.indexOf("-"));
			User u = null;
			String aUserId = AppData.getUser(getActivity())._id;
			for (int i = 0; i < ids.length; i++) {
				if (!aUserId.equals(ids[i])) {
					u = userMap.get(ids[i]);
					break;
				}
			}
			if (u == null)
				return;
			for (int i = 0; i < conversationList.size(); i++) {
				if (u._id.equals(conversationList.get(i).accountId)) {
					conversationInfo = conversationList.get(i);
					break;
				}
			}
			if(conversationInfo == null){
				conversationInfo = new ConversationInfo();
			}
			conversationInfo.friendName = u.userName;
			conversationInfo.GroupPhoto = u.headPhoto;
			conversationInfo.accountId = u._id;
			AppData.chatMap.put(conversationInfo.accountId, chatItemList);
		} else {
			for (int i = 0; i < conversationList.size(); i++) {
				if (chatGroup._id.equals(conversationList.get(i).groupId)) {
					conversationInfo = conversationList.get(i);
					break;
				}
			}
			if(conversationInfo == null){
				conversationInfo = new ConversationInfo();
			}
			conversationInfo.friendName = chatGroup.GroupName;
			conversationInfo.GroupPhoto = chatGroup.GroupPhoto;
			conversationInfo.groupId = chatGroup._id;
			AppData.chatMap.put(conversationInfo.groupId, chatItemList);
		}
		conversationInfo.unread_num = unread_num;
		conversationInfo.content = chatItem.msg;
		conversationInfo.contentType = chatItem.mediaType;
		conversationList.add(conversationInfo);
		System.out.println("add off conversation");
		AppData.conversationList.add(conversationInfo);
		handler.sendEmptyMessage(1);
	}

	OffineMessageManager offineMessageManager = new OffineMessageManager();

	private int getUnReadNum(List<ChatItem> list) {
		int num = 0;
		String userId = AppData.getUser(getActivity())._id;
		for (ChatItem item : list) {
			if (!userId.equals(item.user) && !item.state.equals("2")) {
				num++;
			}
		}
		return num;
	}

	// private final class ConversationTask extends AsyncTask<Integer, Integer,
	// Result>
}
