package com.guotion.sicilia.ui.popupwindow;

import com.guotion.common.utils.CacheUtil;
import com.guotion.sicilia.R;
import com.guotion.sicilia.constants.AndroidRequestCode;
import com.guotion.sicilia.util.AndroidFileUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

public class ChoosePhotoPopupwindow extends PopupWindow {
	private View popView;
	private Activity activity;

	private Button camera;
	private Button album;
	private Button cancel;
	private String imagePath = null;
	private int pictureRequestCode = AndroidRequestCode.REQ_CODE_PICTURES;
	private int cameraRequestCode = AndroidRequestCode.REQ_CODE_CAMERA;
	private int width = 320;
	private int height = 320;
	public ChoosePhotoPopupwindow(Activity activity) {
		super(activity);
		this.activity = activity;
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popView = inflater.inflate(R.layout.popupwindow_choosephoto, null);
		initView();
		initListener();
	}
	/**
	 * 设置图片请求编码
	 * @param pictureRequestCode 常量在AndroidRequestCode
	 */
	public void setPictureRequestCode(int pictureRequestCode){
		this.pictureRequestCode = pictureRequestCode;
	}
	/**
	 * 设置拍照请求编码
	 * @param cameraRequestCode 常量在AndroidRequestCode
	 */
	public void setCameraRequestCode(int cameraRequestCode){
		this.cameraRequestCode = cameraRequestCode;
	}
		
	private void initView() {
		// 让泡泡窗口获取焦点
		super.setFocusable(true);
		// 点击其它地方收起泡泡窗口
		super.setBackgroundDrawable(new BitmapDrawable());
		// 设置弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置PopupWindow的View 
        this.setContentView(popView);
		// 设置弹出窗体动画效果
		// this.setAnimationStyle(R.style.AnimBottom);
		camera = (Button) popView.findViewById(R.id.tv_popup_choose_camera);
		album = (Button) popView.findViewById(R.id.tv_popup_choose_album);
		cancel = (Button) popView.findViewById(R.id.tv_popup_choose_cancel);
	}

	private void initListener() {
		camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imagePath = CacheUtil.chatImageCachePath +"/"+ System.currentTimeMillis() + ".jpg";
				AndroidFileUtils.openCamera(activity, imagePath, cameraRequestCode);
				dismiss();
			}
		});

		album.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (pictureRequestCode/1000 == 1) {
					AndroidFileUtils.openPicturesChooser(activity, pictureRequestCode);
				}else if(pictureRequestCode/1000 == 2){
					imagePath = CacheUtil.avatarCachePath + "/"+  System.currentTimeMillis() + ".jpg";
					AndroidFileUtils.openPicturesChooser(activity, imagePath, width, height, pictureRequestCode);
				}
				
				dismiss();
			}
		});

		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
	
	public String getImagePath(){
		return imagePath;
	}
}