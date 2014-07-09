package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.InviteMember;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.ui.dialog.InviteMembersDialog;
import com.guotion.sicilia.util.PreferencesHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @function 邀请成员中listview的adapter
 *
 * @author   Longhai.Yin  MailTo: 1195219040 @ qq.com
 *
 * @version  NO.01
 *
 * @create   2014-4-7 下午5:09:42
 *
 */
public class InviteMemberAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<User> list;
	private ArrayList<User> choose;
	private LayoutInflater inflater;
	int theme;
	public InviteMemberAdapter() {}

	public InviteMemberAdapter(Context context, ArrayList<User> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		theme = new PreferencesHelper(context).getInt(AppData.THEME);
		choose = new ArrayList<User>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.invite_member_item, null);
			holder = new ViewHolder();
			holder.listImage = (ImageView) convertView
					.findViewById(R.id.img_member_head);
			holder.listName = (TextView) convertView
					.findViewById(R.id.txt_member_name);
			convertView.setTag(holder);
			holder.listCheckbox = (CheckBox) convertView
					.findViewById(R.id.select_member_box);
			try{
				holder.listCheckbox.setButtonDrawable(AppData.getThemeImgResId(theme, "checkbox_style"));
			}catch(Exception e){
				e.printStackTrace();
			}
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final User entity = list.get(position);
		// 设置头像 图片
		String imgUrl = entity.headPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
			if(bitmap == null){
				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+entity.headPhoto,holder.listImage,R.drawable.head_m,R.drawable.head_m);
			}else{
				holder.listImage.setImageBitmap(bitmap);
			}
		}else{
			holder.listImage.setImageResource(R.drawable.head_m);
		}
//		Bitmap bitmap = BitmapFactory.decodeFile(entity.headPhoto);
//		if (bitmap != null) {
//			holder.listImage.setImageBitmap(bitmap);
//		}		
		holder.listName.setText(entity.userName);
		holder.listName.setTextColor(Color.BLACK);
		holder.listCheckbox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox)v;
				if(cb.isChecked()){
					choose.add(list.get(position));
				}else{
					choose.remove(list.get(position));
				}
			}
		});
		// 点击条目判断条目上的checkBox是否选中
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(holder.listCheckbox.isChecked()){
					holder.listCheckbox.setChecked(false);
					choose.remove(list.get(position));
				} else {
					holder.listCheckbox.setChecked(true);
					choose.add(list.get(position));
				}
			}
		});
		return convertView;
	}
	
	public ArrayList<User> getChoose() {
		return choose;
	}
	
	static class ViewHolder {
		public ImageView listImage;
		public TextView listName;
		public CheckBox listCheckbox;
	}
}
