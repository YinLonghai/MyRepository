package com.guotion.sicilia.ui.fragment;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.ChatServer;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.im.util.OffineMessageManager;
import com.guotion.sicilia.ui.RegisterSettingActivity;
import com.guotion.sicilia.ui.SetThemeActivity;
import com.guotion.sicilia.ui.TagActivity;
import com.guotion.sicilia.ui.UserCloudsActivity;
import com.guotion.sicilia.ui.dialog.AccountManageDialog;
import com.guotion.sicilia.ui.dialog.AccountManageDialog.OnLogoutListerner;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.UISkip;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingFragment extends Fragment {
	private View contentView;
	private ImageView head;
	private ImageView cloud;
	private ImageView register;
	private ImageView info;
	private ImageView tag;
	private ImageView theme;
	private TextView top;
	private TextView name;
	private ImageView imageView1;
	private AccountManageDialog accountManageDialog = null;
	private int themeType;
	private User user = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		contentView = inflater.inflate(R.layout.fragment_main_setting, container,false);
		initData();
		initView(contentView);
		initListener();
		return contentView;
	}
	@Override
	public void onResume() {
		int theme = new PreferencesHelper(getActivity()).getInt(AppData.THEME);//System.out.println("SettingFragment===old-"+themeType+"new-"+theme);
		if(themeType != theme){
			themeType = theme;//System.out.println("SettingFragment===change to "+themeType);
			updateTheme();
		}
		super.onResume();
	}
	private void initData() {
		user = AppData.getUser(getActivity());
		themeType = new PreferencesHelper(getActivity()).getInt(AppData.THEME);
	}
	private void initView(View contentView) {
		name = (TextView) contentView.findViewById(R.id.textView_set_admin);
		head = (ImageView) contentView.findViewById(R.id.imageView_head);
		cloud = (ImageView) contentView.findViewById(R.id.imageView_cloud);
		register = (ImageView) contentView.findViewById(R.id.imageView_register);
		info = (ImageView) contentView.findViewById(R.id.imageView_info);
		tag = (ImageView) contentView.findViewById(R.id.imageView_tag);
		theme = (ImageView) contentView.findViewById(R.id.imageView_theme);
		top = (TextView) contentView.findViewById(R.id.textView1);
		imageView1 = (ImageView) contentView.findViewById(R.id.imageView1);
//		if(AppData.USER.level.equals("1")){
//			register.setVisibility(View.GONE);
//			tag.setVisibility(View.GONE);
//		}
		name.setText(user.userName);
		String imgUrl = user.headPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
			if(bitmap == null){
				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+ user.headPhoto, head, R.drawable.head_s, R.drawable.head_s);
			}else{
				head.setImageBitmap(bitmap);
			}
		}else{
			head.setImageResource(R.drawable.head_s);
		}
		
		updateTheme();
	}
	private void updateTheme(){
		try{
			switch(themeType){
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(themeType));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(themeType, "bg_title"));
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(themeType, "bg_title"));
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(themeType));
				break;
			}
			name.setTextColor(getResources().getColor(AppData.getThemeColor(themeType)));
			cloud.setImageResource(AppData.getThemeImgResId(themeType, "my_cloud"));
			register.setImageResource(AppData.getThemeImgResId(themeType, "registration_management"));
			info.setImageResource(AppData.getThemeImgResId(themeType, "my_profile"));
			tag.setImageResource(AppData.getThemeImgResId(themeType, "tag_management"));
			theme.setImageResource(AppData.getThemeImgResId(themeType, "change_theme"));
			imageView1.setImageResource(AppData.getThemeImgResId(themeType, "set_up_back"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initListener() {
		head.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				UISkip.skip(false, getActivity(), AccountManageActivity.class);
				if (accountManageDialog == null) {
					accountManageDialog = new AccountManageDialog(getActivity());
					accountManageDialog.setOnLogoutListerner(new OnLogoutListerner() {
						@Override
						public void onLogout() {
							AppData.chatMap.clear();
							AppData.conversationList.clear();
							new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										new OffineMessageManager().sendLastRead(AppData.lastReadMap, AppData.getUser(getActivity())._id);
										ChatServer.getInstance().logout();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}).start();
							getActivity().finish();
						}
					});
				}
				accountManageDialog.show();
			}
		});
		cloud.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppData.tempuser = user;
				UISkip.skip(false, getActivity(), UserCloudsActivity.class);
			}
		});
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UISkip.skip(false, getActivity(), RegisterSettingActivity.class);
			}
		});
		info.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UISkip.skipToUserInfoActivity(getActivity());
			}
		});
		tag.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UISkip.skip(false, getActivity(), TagActivity.class);
			}
		});
		theme.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UISkip.skip(false, getActivity(), SetThemeActivity.class);
			}
		});
	}
	public void notifyUserChange() {
		user = AppData.getUser(getActivity());
		name.setText(user.userName);
		String imgUrl = user.headPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
			if(bitmap == null){
				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+ user.headPhoto, head, R.drawable.head_s, R.drawable.head_s);
			}else{
				head.setImageBitmap(bitmap);
			}
		}else{
			head.setImageResource(R.drawable.head_s);
		}
	}
}
